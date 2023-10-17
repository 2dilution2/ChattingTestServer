package com.chat.chattingtest2.domain.crew.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.chat.chattingtest2.domain.crew.model.dto.CrewListRes;
import com.chat.chattingtest2.domain.crew.model.dto.CrewReq;
import com.chat.chattingtest2.domain.crew.model.dto.CrewRes;
import com.chat.chattingtest2.domain.crew.model.entity.CrewMessage;

public interface CrewService {

	CrewRes createCrew(CrewReq requestDto, String email);


	void enterCrewChat(Long crewId, String token);

	List<CrewMessage> getChatHistory(Long crewId, int page, int size);

	List<CrewListRes> getCrewList(String keyword, Pageable pageable);

}
