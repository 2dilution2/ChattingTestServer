package com.chat.chattingtest2.global.error.exception;

import com.chat.chattingtest2.global.error.type.CrewErrorCode;

import lombok.Getter;

@Getter
public class CrewException extends RuntimeException {
	private final CrewErrorCode errorCode;

	public CrewException(CrewErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}