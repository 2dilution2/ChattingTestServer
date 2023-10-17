package com.chat.chattingtest2.domain.crew.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.chattingtest2.domain.crew.model.entity.CrewMember;
import com.chat.chattingtest2.domain.crew.model.entity.CrewMemberId;

public interface CrewMemberRepository extends JpaRepository<CrewMember, CrewMemberId> {
	Long countByIdCrewId(Long crewId);
	boolean existsById(CrewMemberId crewMemberId);
}
