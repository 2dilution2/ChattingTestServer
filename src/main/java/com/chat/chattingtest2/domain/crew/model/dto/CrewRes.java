package com.chat.chattingtest2.domain.crew.model.dto;

import com.chat.chattingtest2.domain.crew.model.entity.Crew;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CrewRes {

	private Long id;
	private String title;
	private Long memberId;
	private Integer maxCrew;
	private String crewContent;

	public static CrewRes fromEntity(Crew crew) {
		return CrewRes.builder()
			.id(crew.getCrewId())
			.title(crew.getTitle())
			.memberId(crew.getMember().getId())
			.maxCrew(crew.getMaxCrew())
			.crewContent(crew.getCrewContent())
			.build();
	}

	public CrewRes(Crew crew){
		this.title = crew.getTitle();
		this.memberId = crew.getMember().getId();
		this.maxCrew = crew.getMaxCrew();
		this.crewContent = crew.getCrewContent();

	}

}
