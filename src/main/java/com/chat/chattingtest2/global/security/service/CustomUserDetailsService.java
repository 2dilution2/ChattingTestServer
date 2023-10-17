package com.chat.chattingtest2.global.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chat.chattingtest2.domain.member.model.entity.Member;
import com.chat.chattingtest2.domain.member.repository.MemberRepository;
import com.chat.chattingtest2.global.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private static final String MESSAGE = "Invalid Authentication";

	private final MemberRepository memberRepository;

	@Override
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = (Member)memberRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(MESSAGE));

		return new CustomUserDetails(member);
	}
}
