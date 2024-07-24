package com.example.fantreehouse.common.exception;

import com.example.fantreehouse.common.exception.errorcode.AuthorizedException;
import com.example.fantreehouse.common.exception.errorcode.CommonErrorCode;
import com.example.fantreehouse.common.exception.errorcode.DuplicatedException;
import com.example.fantreehouse.common.exception.errorcode.ErrorCode;
import com.example.fantreehouse.common.exception.errorcode.ErrorResponse;
import com.example.fantreehouse.common.exception.errorcode.MismatchException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.PatternMismatchException;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

  // NotFoundException 예외처리
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleCustomException(NotFoundException e) {
    log.warn("Not Found Exception");
    ErrorCode errorCode = e.getErrorCode();
    return handleExceptionInternal(errorCode, e);
  }

  // MismatchException 예외처리
  @ExceptionHandler(MismatchException.class)
  public ResponseEntity<Object> handleCustomException(MismatchException e) {
    log.warn("Mismatch Exception");
    ErrorCode errorCode = e.getErrorCode();
    return handleExceptionInternal(errorCode, e);
  }

  // DuplicatedException 예외처리
  @ExceptionHandler(DuplicatedException.class)
  public ResponseEntity<Object> handleCustomException(DuplicatedException e) {
    log.warn("Duplicated Exception");
    ErrorCode errorCode = e.getErrorCode();
    return handleExceptionInternal(errorCode, e);
  }

  // PatternMismatchException 예외처리
  @ExceptionHandler(PatternMismatchException.class)
  public ResponseEntity<Object> handleCustomException(PatternMismatchException e) {
    log.warn("Pattern Mismatch Exception");
    ErrorCode errorCode = e.getErrorCode();
    return handleExceptionInternal(errorCode, e);
  }


  // AuthorizedException 예외처리
  @ExceptionHandler(AuthorizedException.class)
  public ResponseEntity<Object> handleCustomException(AuthorizedException e) {
    log.warn("Authorized Exception");
    ErrorCode errorCode = e.getErrorCode();
    return handleExceptionInternal(errorCode, e);
  }

  // MethodArgumentNotValidException 예외처리
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.warn("Method Argument Not Valid Exception");
    ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
    return handleExceptionInternal(errorCode, e);
  }

  // 그 외 예외처리들
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAllException(Exception e) {
    log.warn("handleAllException: {}", e.getMessage());
    ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
    return handleExceptionInternal(errorCode, e);
  }

  // ResponseEntity 생성 함수
  private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, Exception e) {
    int value = errorCode.getHttpStatus().value();
    String message = e.getMessage();

    if (e instanceof MethodArgumentNotValidException) {
      message = "Method Argument Not Valid Exception";
    }

    HttpStatus httpStatus = errorCode.getHttpStatus();

    if (message == null) {
      message = errorCode.getMessage();
    }

    ErrorResponse.ErrorResponseBuilder builder = ErrorResponse.builder()
        .code(value)
        .message(message)
        .httpStatus(httpStatus);

    // BindException 일 경우 Validation 에러 리스트도 함께 출력
    if (e instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
      List<ErrorResponse.ValidationError> validationErrorList = methodArgumentNotValidException.getBindingResult()
          .getFieldErrors()
          .stream()
          .map(ErrorResponse.ValidationError::of)
          .toList();

      builder.errors(validationErrorList);
    }

    return ResponseEntity.status(httpStatus).body(builder.build());
  }

}
