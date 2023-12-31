package com.chat.chattingtest2.domain.member.model.dto;

import com.chat.chattingtest2.domain.member.model.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SignUpRes {
	private String email;
	private String nickname;

	public SignUpRes(Member member) {
		this.email = member.getEmail();
		this.nickname = member.getNickname();
	}
}
