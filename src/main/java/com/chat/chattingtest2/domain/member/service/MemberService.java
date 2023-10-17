package com.chat.chattingtest2.domain.member.service;

import static com.chat.chattingtest2.global.error.type.MemberErrorCode.*;

import java.util.Objects;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.chat.chattingtest2.domain.member.model.constants.MemberRole;
import com.chat.chattingtest2.domain.member.model.dto.LoginReq;
import com.chat.chattingtest2.domain.member.model.dto.LoginRes;
import com.chat.chattingtest2.domain.member.model.dto.MemberInfoResponse;
import com.chat.chattingtest2.domain.member.model.dto.ReissueRes;
import com.chat.chattingtest2.domain.member.model.entity.Member;
import com.chat.chattingtest2.domain.member.repository.MemberRepository;
import com.chat.chattingtest2.global.error.exception.MemberException;
import com.chat.chattingtest2.global.error.type.MemberErrorCode;
import com.chat.chattingtest2.global.security.jwt.JwtProvider;
import com.chat.chattingtest2.global.security.jwt.model.BlockAccessToken;
import com.chat.chattingtest2.global.security.jwt.model.RefreshToken;
import com.chat.chattingtest2.global.security.jwt.repository.BlockAccessTokenRepository;
import com.chat.chattingtest2.global.security.jwt.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final JwtProvider jwtProvider;
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final BlockAccessTokenRepository blockAccessTokenRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	public Member getByCredential(String email) {
		return (Member)memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
	}

	public LoginRes login(LoginReq request) {

		Member member = (Member)memberRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			throw new MemberException(WRONG_MEMBER_PASSWORD);
		}

		String accessToken = jwtProvider.createAccessToken(member.getEmail());
		String refreshToken = jwtProvider.createRefreshToken(member.getEmail());

		return LoginRes.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public void logout(String request) {

		String accessToken = request.substring(7);
		String email = jwtProvider.getEmail(accessToken);

		BlockAccessToken blockAccessToken = BlockAccessToken.builder()
			.id(accessToken)
			.email(email)
			.expiration(jwtProvider.getRemainingTime(accessToken))
			.build();

		refreshTokenRepository.deleteById(email);
		blockAccessTokenRepository.save(blockAccessToken);
	}

	public ReissueRes reissueAccessToken(String refreshToken) {

		String email = jwtProvider.getEmail(refreshToken);

		RefreshToken refreshTokenInRedis
			= refreshTokenRepository.findById(email)
			.orElseThrow(() -> new MemberException(FAIL_TO_REISSUE_TOKEN));

		if (!Objects.equals(refreshTokenInRedis.getToken(), refreshToken)) {
			log.error("rtk in redis and rtk in request header are not equal");
			throw new MemberException(FAIL_TO_REISSUE_TOKEN);
		}

		String newAccessToken = jwtProvider.createRefreshToken(email);
		return ReissueRes.builder()
			.accessToken(newAccessToken)
			.build();
	}

	public Member signUp(Member member) {
		String rawPw = member.getPassword();
		member.setPassword(passwordEncoder.encode(rawPw));
		member.setRole(MemberRole.USER);
		return memberRepository.save(member);
	}

	public MemberInfoResponse getMemberInfo(Member member) {
		return MemberInfoResponse.builder()
			.nickname(member.getNickname())
			.build();
	}
}
