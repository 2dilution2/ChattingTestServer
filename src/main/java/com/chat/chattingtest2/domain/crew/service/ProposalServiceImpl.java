package com.chat.chattingtest2.domain.crew.service;

import static com.chat.chattingtest2.domain.crew.model.constants.ProposalStatus.*;
import static com.chat.chattingtest2.global.error.type.CrewErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat.chattingtest2.domain.crew.model.dto.AddProposalReq;
import com.chat.chattingtest2.domain.crew.model.dto.EditProposalStatusReq;
import com.chat.chattingtest2.domain.crew.model.dto.ProposalRes;
import com.chat.chattingtest2.domain.crew.model.entity.Crew;
import com.chat.chattingtest2.domain.crew.model.entity.CrewMember;
import com.chat.chattingtest2.domain.crew.model.entity.Proposal;
import com.chat.chattingtest2.domain.crew.repository.CrewMemberRepository;
import com.chat.chattingtest2.domain.crew.repository.CrewRepository;
import com.chat.chattingtest2.domain.crew.repository.ProposalRepository;
import com.chat.chattingtest2.domain.member.model.entity.Member;
import com.chat.chattingtest2.domain.member.repository.MemberRepository;
import com.chat.chattingtest2.global.error.exception.CrewException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

	private final CrewRepository crewRepository;
	private final ProposalRepository proposalRepository;
	private final CrewMemberRepository crewMemberRepository;
	private final MemberRepository memberRepository;
	private final RabbitTemplate rabbitTemplate;

	@Override
	@Transactional
	public Map<String, String> addProposal(Long crewId, AddProposalReq request, Member member) {

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		// 자신이 작성한 동행에 신청 불가
		if (Objects.equals(crew.getMember().getEmail(), member.getEmail())) {
			throw new CrewException(FAIL_TO_APPLY_CREW);
		}

		// 해당 동행을 이미 신청한 경우 중복 불가
		if (proposalRepository.existsByCrewAndMember(crew, member)) {
			throw new CrewException(ALREADY_APPLIED_MEMBER);
		}

		Proposal proposal = Proposal.builder()
			.crew(crew)
			.member(member)
			.content(request.getContent())
			.status(WAITING)
			.build();

		proposalRepository.save(proposal);
		// 추후 알림 전송 예정
		return getMessage(String.format("%s님 신청이 완료되었습니다.", member.getNickname()));
	}

	@Override
	@Transactional
	public Map<String, String> cancelProposal(Long crewId, Member member) {

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		Proposal proposal = proposalRepository.findByCrewAndMember(crew, member)
			.orElseThrow(() -> new CrewException(PROPOSAL_MEMBER_NOT_FOUND));

		// 신청자가 아니면 취소할 수 없음
		if (!Objects.equals(proposal.getMember().getEmail(), member.getEmail())) {
			throw new CrewException(FAIL_TO_CANCEL_PROPOSAL);
		}

		proposalRepository.delete(proposal);
		return getMessage(String.format("%s님 신청이 취소되었습니다.", member.getNickname()));
	}

	@Override
	@Transactional
	public Map<String, String> approveProposal(Long crewId, EditProposalStatusReq request, Member member) {

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		// 동행 작성자만 승인 가능
		if (!Objects.equals(member.getEmail(), crew.getMember().getEmail())) {
			throw new CrewException(FAIL_TO_APPROVE_CREW);
		}

		// 신청 승인이 가능한 상태인지 확인 (= WAITING)
		Proposal proposal
			= proposalRepository.findByCrewIdAndNicknameAndProposalStatus(crewId, request.getNickname(), WAITING)
			.orElseThrow(() -> new CrewException(IMPOSSIBLE_TO_APPROVE_MEMBER));

		proposal.approve();

		// crewMember에 초대
		Member proposer = memberRepository.findByNickname(request.getNickname())
			.orElseThrow(() -> new CrewException(PROPOSAL_MEMBER_NOT_FOUND));

		CrewMember crewMember = new CrewMember(proposer.getId(), crew.getCrewId());

		// 크루 멤버 초과시
		if (crewMemberRepository.countByIdCrewId(crew.getCrewId()) >= crew.getMaxCrew()) {
			throw new CrewException(CREW_EXCEEDED_MAX);
		}

		// 크루 멤버가 아닐 경우 초대
		if (!crewMemberRepository.existsById(crewMember.getId())) {
			crewMemberRepository.save(crewMember);

			// RabbitMQ에 입장 메시지 전송
			String entranceMessage = proposer.getNickname() + "님이 크루에 참여하였습니다.";
			rabbitTemplate.convertAndSend("CHAT_EXCHANGE_NAME", "chat." + crewId, entranceMessage);
		} else {
			throw new CrewException(ALREADY_APPROVE);
		}

		return getMessage(String.format("%s님의 동행 신청을 수락하였습니다.", request.getNickname()));
	}

	@Override
	public Map<String, String> rejectProposal(Long crewId, EditProposalStatusReq request, Member member) {

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		// 동행 작성자만 거절 가능
		if (!Objects.equals(member.getEmail(), crew.getMember().getEmail())) {
			throw new CrewException(FAIL_TO_REJECT_CREW);
		}

		// 신청 거절이 가능한 상태인지 확인 (= WAITING)
		Proposal proposal
			= proposalRepository.findByCrewIdAndNicknameAndProposalStatus(crewId, request.getNickname(), WAITING)
			.orElseThrow(() -> new CrewException(IMPOSSIBLE_TO_REJECT_MEMBER));

		proposal.reject();
		return getMessage(String.format("%s님의 동행 신청을 거절하였습니다.", request.getNickname()));
	}

	@Override
	public List<ProposalRes> getProposalList(Long crewId) {

		if (!crewRepository.existsById(crewId)) {
			throw new CrewException(CREW_NOT_FOUND);
		}

		return proposalRepository.findAllByCrewId(crewId);
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}


/* 수정 필요
	// 나를 제외한 동행 참여 인원 리스트
	@Transactional
	public List<Object> crewMemberList(long crewId, String email) {
		Crew crew = crewRepository.findById(crewId).orElseThrow();
		Optional<Member> optionalMember = memberRepository.findByEmail(email);
		if (optionalMember.isPresent()) {
			Member member = optionalMember.get();
			List<CrewMember> list = proposalRepository.findAllByCrewAndMemberNotAndStatus(crew, member, APPROVED);
			return list.stream().map(CrewMemberRes::fromEntity).collect(Collectors.toList());
		} else {
			throw new MemberException(MEMBER_NOT_FOUND);
		}
	}
 */
}
