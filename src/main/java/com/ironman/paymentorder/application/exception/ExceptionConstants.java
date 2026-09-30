package com.ironman.paymentorder.application.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionConstants {
  public static final String MESSAGE_INVALID_INPUT_DATA =
      "Invalid input data. Verify format and values.";
  public static final String MESSAGE_BUSINESS_RULE_VIOLATION =
      "Business rule violation. Verify the data does not conflict with existing records.";
  public static final String MESSAGE_UNEXPECTED_ERROR = "Unexpected error. Please try again later.";
}
