package com.chat.chattingtest2.global.error.exception;

import com.chat.chattingtest2.global.error.type.RecordErrorCode;

import lombok.Getter;

@Getter
public class RecordException extends RuntimeException {

	private final RecordErrorCode errorCode;

	public RecordException(RecordErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
