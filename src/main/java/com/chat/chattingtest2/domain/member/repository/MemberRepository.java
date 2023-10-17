package com.chat.chattingtest2.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.chattingtest2.domain.member.model.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Object> findByEmail(String email);

	Optional<Member>  findByNickname(String nickname);
}
