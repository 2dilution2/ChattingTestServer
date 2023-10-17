package com.chat.chattingtest2.global.error.handler;

import static com.chat.chattingtest2.global.error.type.CommonErrorCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.chat.chattingtest2.global.error.exception.CommonException;
import com.chat.chattingtest2.global.error.exception.CrewException;
import com.chat.chattingtest2.global.error.exception.MemberException;
import com.chat.chattingtest2.global.error.exception.RecordException;
import com.chat.chattingtest2.global.error.model.ErrorResponse;
import com.chat.chattingtest2.global.error.model.FieldErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MemberException.class)
	public ResponseEntity<ErrorResponse> handleMemberException(MemberException e) {

		ErrorResponse response = ErrorResponse.builder()
			.status(e.getErrorCode().getStatus().value())
			.code(e.getErrorCode().getCode())
			.message(e.getErrorCode().getMessage())
			.build();

		return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
	}

	@ExceptionHandler(RecordException.class)
	public ResponseEntity<ErrorResponse> handleTravelRecordException(RecordException e) {

		ErrorResponse response = ErrorResponse.builder()
			.status(e.getErrorCode().getStatus().value())
			.code(e.getErrorCode().getCode())
			.message(e.getErrorCode().getMessage())
			.build();

		return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
	}

	// 유효성 검증에 대한 예외
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<FieldErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

		FieldErrorResponse response = FieldErrorResponse.from(INVALID_ARGUMENT, e.getFieldErrors());

		return ResponseEntity.status(INVALID_ARGUMENT.getStatus()).body(response);
	}

	// 이미지 업로드하는 경우 파일 크기 초과 시 발생하는 예외
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException() {

		ErrorResponse response = ErrorResponse.builder()
			.status(IMAGE_SIZE_LIMIT_EXCEEDED.getStatus().value())
			.code(IMAGE_SIZE_LIMIT_EXCEEDED.getCode())
			.message(IMAGE_SIZE_LIMIT_EXCEEDED.getMessage())
			.build();

		return ResponseEntity.status(IMAGE_SIZE_LIMIT_EXCEEDED.getStatus()).body(response);
	}

	@ExceptionHandler(CrewException.class)
	public ResponseEntity<ErrorResponse> handleCrewException(CrewException e) {

		ErrorResponse response = ErrorResponse.builder()
			.status(e.getErrorCode().getStatus().value())
			.code(e.getErrorCode().getCode())
			.message(e.getErrorCode().getMessage())
			.build();

		return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
	}

	@ExceptionHandler(CommonException.class)
	public ResponseEntity<ErrorResponse> handleCommonException(CommonException e) {

		ErrorResponse response = ErrorResponse.builder()
			.status(e.getErrorCode().getStatus().value())
			.code(e.getErrorCode().getCode())
			.message(e.getErrorCode().getMessage())
			.build();

		return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
	}
}