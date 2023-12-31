package com.chat.chattingtest2.domain.crew.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.chattingtest2.domain.crew.model.dto.AddProposalReq;
import com.chat.chattingtest2.domain.crew.model.dto.EditProposalStatusReq;
import com.chat.chattingtest2.domain.crew.model.dto.ProposalRes;
import com.chat.chattingtest2.domain.crew.service.ProposalService;
import com.chat.chattingtest2.global.security.CustomUserDetails;
import com.chat.chattingtest2.global.util.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class ProposalController {

	private final ProposalService proposalService;

	@PostMapping("/{crewId}/apply")
	public ResponseEntity<Map<String, String>> addProposal(@PathVariable Long crewId,
		@RequestBody @Valid AddProposalReq request, @AuthUser CustomUserDetails userDetails) {

		Map<String, String> response = proposalService.addProposal(crewId, request, userDetails.getMember());
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{crewId}/apply")
	public ResponseEntity<Map<String, String>> cancelProposal(@PathVariable Long crewId,
		@AuthUser CustomUserDetails userDetails) {

		Map<String, String> response = proposalService.cancelProposal(crewId, userDetails.getMember());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{crewId}/list")
	public ResponseEntity<List<ProposalRes>> getProposalList(@PathVariable Long crewId) {

		List<ProposalRes> response = proposalService.getProposalList(crewId);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{crewId}/approve")
	public ResponseEntity<Map<String, String>> approveProposal(@PathVariable Long crewId,
		@RequestBody EditProposalStatusReq request, @AuthUser CustomUserDetails userDetails) {

		Map<String, String> response = proposalService.approveProposal(crewId, request, userDetails.getMember());
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{crewId}/reject")
	public ResponseEntity<Map<String, String>> rejectProposal(@PathVariable Long crewId,
		@RequestBody EditProposalStatusReq request, @AuthUser CustomUserDetails userDetails) {

		Map<String, String> response = proposalService.rejectProposal(crewId, request, userDetails.getMember());
		return ResponseEntity.ok(response);
	}

	// 나를 제외한 동행 참여 인원 리스트

/*
	@GetMapping("/{crewId}/members")
	public ResponseEntity<List<Object>> CrewMemberList(@PathVariable long crewId, Principal principal) {
		return ResponseEntity.ok(proposalService.crewMemberList(crewId, principal.getName()));
	}
*/
}
