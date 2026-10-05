package com.ironman.paymentorder.application.exception;

import static com.ironman.paymentorder.application.exception.ApplicationException.ExceptionType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCatalog {
  EVENT_SERVICE_ERROR(
      "POSER0001",
      ExceptionType.INTERNAL_SERVER_ERROR,
      "An unexpected error occurred in the event service."),
  APPLICATION_ERROR(
      "POSER0002",
      ExceptionType.INTERNAL_SERVER_ERROR,
      "An unexpected error occurred, please try again later."),
  CURRENT_ACCOUNT_SERVICE(
      "POSER0003",
      ExceptionType.INTERNAL_SERVER_ERROR,
      "An unexpected error occurred in the current account service."),
  CURRENT_ACCOUNT_EMPTY(
      "POSER0004",
      ExceptionType.CONFLICT,
      "The current account service returned an empty response."),
  PARTY_REFERENCE_SERVICE(
      "POSER0005",
      ExceptionType.INTERNAL_SERVER_ERROR,
      "An unexpected error occurred in the party reference service."),
  PARTY_REFERENCE_EMPTY(
      "POSER0006",
      ExceptionType.CONFLICT,
      "The party reference service returned an empty response.");

  private final String code;
  private final ExceptionType exceptionType;
  private final String message;

  public ApplicationException buildException(Object... args) {
    String formattedMessage = String.format(message, args);

    return new ApplicationException(code, exceptionType, formattedMessage);
  }
}
