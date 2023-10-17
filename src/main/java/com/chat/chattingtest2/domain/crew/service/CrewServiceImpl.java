package com.chat.chattingtest2.domain.crew.service;

import static com.chat.chattingtest2.global.error.type.CrewErrorCode.*;
import static com.chat.chattingtest2.global.error.type.MemberErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.chat.chattingtest2.domain.crew.model.dto.CrewListRes;
import com.chat.chattingtest2.domain.crew.model.dto.CrewReq;
import com.chat.chattingtest2.domain.crew.model.dto.CrewRes;
import com.chat.chattingtest2.domain.crew.model.entity.Crew;
import com.chat.chattingtest2.domain.crew.model.entity.CrewMember;
import com.chat.chattingtest2.domain.crew.model.entity.CrewMemberId;
import com.chat.chattingtest2.domain.crew.model.entity.CrewMessage;
import com.chat.chattingtest2.domain.crew.model.entity.QCrewMessage;
import com.chat.chattingtest2.domain.crew.repository.CrewMemberRepository;
import com.chat.chattingtest2.domain.crew.repository.CrewRepository;
import com.chat.chattingtest2.domain.member.model.entity.Member;
import com.chat.chattingtest2.domain.member.repository.MemberRepository;
import com.chat.chattingtest2.global.error.exception.CommonException;
import com.chat.chattingtest2.global.error.exception.CrewException;
import com.chat.chattingtest2.global.error.exception.MemberException;
import com.chat.chattingtest2.global.error.type.CommonErrorCode;
import com.chat.chattingtest2.global.security.jwt.JwtProvider;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrewServiceImpl implements CrewService {

	private final CrewRepository crewRepository;
	private final MemberRepository memberRepository;
	private final CrewMemberRepository crewMemberRepository;
	private final JwtProvider jwtProvider;
	private final JPAQueryFactory queryFactory;

	@Override
	@Transactional
	public CrewRes createCrew(CrewReq requestDto, String email) {

		Member member = (Member)memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Crew crew = Crew.builder()
			.title(requestDto.getTitle())
			.maxCrew(requestDto.getMaxCrew())
			.crewContent(requestDto.getCrewContent())
			.build();

		crew.setMember(member);
		crewRepository.save(crew);

		// 작성자가 크루 멤버에 Owner로 저장
		CrewMemberId crewMemberId = new CrewMemberId(member.getId(), crew.getCrewId());

		CrewMember crewMember = CrewMember.builder()
			.id(crewMemberId)
			.isOwner(true)
			.build();

		crewMemberRepository.save(crewMember);

		return CrewRes.fromEntity(crew);
	}

	// 전체 조회
	@Override
	@Transactional
	public List<CrewListRes> getCrewList(String keyword, Pageable pageable) {
		List<Crew> list = crewRepository.findByKeyword(keyword, pageable);
		return list.stream().map(CrewListRes::getEntity).collect(Collectors.toList());
	}

	public long validateToken() {
		// 토큰에 담긴 사용자 정보가 실제로 member 테이블에 존재하는지 여부를 검증
		String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Member member = (Member)memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
		if (Objects.isNull(member)) {
			throw new MemberException(MEMBER_NOT_FOUND);
		}
		return member.getId();
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}

	// 채팅방 입장
	@Override
	@Transactional
	public void enterCrewChat(Long crewId, String token) {
		// 1. Token에서 사용자 인증 정보 가져오기
		if (!jwtProvider.validateToken(token)) {
			throw new CommonException(CommonErrorCode.FAIL_TO_AUTHENTICATION);
		}
		String userEmail = jwtProvider.getEmail(token);
		Member member = (Member)memberRepository.findByEmail(userEmail)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		// 2. 사용자의 채팅방 접근 권한 확인
		CrewMemberId crewMemberId = new CrewMemberId(member.getId(), crewId);
		boolean isCrewMember = crewMemberRepository.existsById(crewMemberId);
		if (!isCrewMember) {
			throw new CrewException(FAIL_ENTER);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<CrewMessage> getChatHistory(Long crewId, int page, int size) {
		QCrewMessage qCrewMessage = QCrewMessage.crewMessage;

		return queryFactory
			.selectFrom(qCrewMessage)
			.where(qCrewMessage.crewMember.id.crewId.eq(crewId))
			.orderBy(qCrewMessage.createdAt.desc())
			.offset(page * size)
			.limit(size)
			.fetch();
	}

}
