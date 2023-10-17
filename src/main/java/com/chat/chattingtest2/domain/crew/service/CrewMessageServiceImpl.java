package com.chat.chattingtest2.domain.crew.service;

import static com.chat.chattingtest2.global.error.type.CrewErrorCode.*;
import static com.chat.chattingtest2.global.error.type.MemberErrorCode.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat.chattingtest2.domain.crew.model.dto.CrewMessageReq;
import com.chat.chattingtest2.domain.crew.model.dto.CrewMessageRes;
import com.chat.chattingtest2.domain.crew.model.entity.Crew;
import com.chat.chattingtest2.domain.crew.model.entity.CrewMember;
import com.chat.chattingtest2.domain.crew.model.entity.CrewMessage;
import com.chat.chattingtest2.domain.crew.repository.CrewMessageRepository;
import com.chat.chattingtest2.domain.crew.repository.CrewRepository;
import com.chat.chattingtest2.domain.member.model.entity.Member;
import com.chat.chattingtest2.domain.member.repository.MemberRepository;
import com.chat.chattingtest2.global.error.exception.CrewException;
import com.chat.chattingtest2.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewMessageServiceImpl implements CrewMessageService {

	private final static String CHAT_EXCHANGE_NAME = "chat.exchange";

	private final CrewRepository crewRepository;
	private final MemberRepository memberRepository;
	private final RabbitTemplate rabbitTemplate;
	private final CrewMessageRepository crewMessageRepository;

	@Override
	@Transactional
	public void send(CrewMessageReq request, Long crewId) {
		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		Member member = memberRepository.findByNickname(request.getSender())
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		CrewMessage message = saveChatMessage(crew, member, request.getContent());

		CrewMessageRes crewMessageRes = CrewMessageRes.builder()
			.content(message.getContent())
			.nickname(member.getNickname())
			.createdAt(message.getCreatedAt())
			.build();

		rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "chat.room." + crewId, crewMessageRes);
	}

	private CrewMessage saveChatMessage(Crew crew, Member member, String content) {

		CrewMember crewMember = new CrewMember(member.getId(), crew.getCrewId());

		CrewMessage message = CrewMessage.builder()
			.content(content)
			.crewMember(crewMember)
			.build();

		return crewMessageRepository.save(message);
	}
}
