package com.chat.chattingtest2.domain.crew.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import com.chat.chattingtest2.global.entity.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrewMessage extends BaseTime {

	@Id
	@Column(name = "crew_message_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "crew_member_id"),
		@JoinColumn(name = "crew_id")
	})
	private CrewMember crewMember;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

}
