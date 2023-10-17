package com.chat.chattingtest2.domain.crew.model.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CrewReq {

	private String title;
	private Integer maxCrew;
	private String crewContent;
}
