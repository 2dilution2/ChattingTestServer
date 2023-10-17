package com.chat.chattingtest2.domain.crew.model.dto;

import java.time.LocalDate;

import com.chat.chattingtest2.domain.crew.model.entity.Crew;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CrewListRes {

	private Long crewId;
	private String title;
	private String crewContent;
	private String memberName;
	private String memberEmail;
	private Integer maxCrew;

	public static CrewListRes getEntity(Crew post) {
		return CrewListRes.builder()
			.crewId(post.getCrewId())
			.title(post.getTitle())
			.crewContent(post.getCrewContent())
			.memberName(post.getMember().getNickname())
			.memberEmail(post.getMember().getEmail())
			.maxCrew(post.getMaxCrew())
			.build();
	}
}
