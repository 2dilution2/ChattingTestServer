package com.chat.chattingtest2.domain.crew.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat.chattingtest2.domain.crew.model.dto.CrewListRes;
import com.chat.chattingtest2.domain.crew.model.dto.CrewReq;
import com.chat.chattingtest2.domain.crew.model.dto.CrewRes;
import com.chat.chattingtest2.domain.crew.service.CrewService;
import com.chat.chattingtest2.global.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CrewController {

	private final CrewService crewService;
	private final JwtProvider jwtProvider;

	@GetMapping("/{crewId}/chat")
	public ResponseEntity<Map<String, String>> enterCrewChat(@PathVariable Long crewId, HttpServletRequest request) {
		String token = jwtProvider.resolveToken(request);
		crewService.enterCrewChat(crewId, token);
		Map<String, String> response = new HashMap<>();
		response.put("message", "채팅방에 참여하였습니다.");
		return ResponseEntity.ok(response);
	}

	// 글 조회
	@GetMapping
	public ResponseEntity<List<CrewListRes>> getAllCrewList(
		@RequestParam(value = "keyword", defaultValue = "") String keyword,
		Pageable pageable) {
		return ResponseEntity.ok(crewService.getCrewList(keyword, pageable));
	}

	// 글 등록
	@PostMapping
	public ResponseEntity<CrewRes> createCrew(
		@RequestBody CrewReq request, Principal principal) {
		CrewRes response = crewService.createCrew(request, principal.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
