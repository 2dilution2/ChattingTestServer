package com.chat.chattingtest2.domain.member.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.chat.chattingtest2.domain.member.model.constants.MemberRole;
import com.chat.chattingtest2.domain.member.model.dto.SignUpReq;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "member")
@NoArgsConstructor
@ToString
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(nullable = false, length = 70, unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	private MemberRole role;

	private String password;

	private String nickname;

	public Member(SignUpReq signUpReq) {
		this();
		this.email = signUpReq.getEmail();
		this.password = signUpReq.getPassword();
		this.nickname = signUpReq.getNickname();
	}
}
