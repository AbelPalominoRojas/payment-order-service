package com.ironman.paymentorder.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApplicationException extends RuntimeException {
  private final String code;
  private final ExceptionType exceptionType;
  private final String message;

  @RequiredArgsConstructor
  @Getter
  public enum ExceptionType {
    BAD_REQUEST(400),
    PRECONDITION_FAILED(409),
    INTERNAL_SERVER_ERROR(500);

    private final int statusCode;
  }
}
