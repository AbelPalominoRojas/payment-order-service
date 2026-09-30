package com.ironman.paymentorder.expose.exception;

import static com.ironman.paymentorder.application.exception.ApplicationException.ExceptionType;
import static com.ironman.paymentorder.application.exception.ExceptionConstants.*;
import static jakarta.ws.rs.core.Response.Status;

import com.ironman.paymentorder.application.exception.ApplicationException;
import com.ironman.paymentorder.application.model.api.ExceptionDetail;
import com.ironman.paymentorder.application.model.api.ExceptionResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {

  @Override
  public Response toResponse(ApplicationException exception) {
    var detail = createExceptionDetail(exception);
    ExceptionResponse apiException = createExceptionResponse(exception, List.of(detail));
    Status status = mapToJaxRsExceptionType(exception.getExceptionType());

    return Response.status(status).entity(apiException).build();
  }

  private static ExceptionDetail createExceptionDetail(ApplicationException exception) {
    return new ExceptionDetail().code(exception.getCode()).description(exception.getMessage());
  }

  private ExceptionResponse createExceptionResponse(
      ApplicationException exception, List<ExceptionDetail> details) {
    String description = descriptionFromExceptionType(exception.getExceptionType());

    return new ExceptionResponse().message(description).details(details);
  }

  private String descriptionFromExceptionType(ExceptionType exceptionType) {
    return switch (exceptionType) {
      case BAD_REQUEST -> MESSAGE_INVALID_INPUT_DATA;
      case PRECONDITION_FAILED -> MESSAGE_BUSINESS_RULE_VIOLATION;
      default -> MESSAGE_UNEXPECTED_ERROR;
    };
  }

  private Status mapToJaxRsExceptionType(ExceptionType exceptionType) {
    return switch (exceptionType) {
      case BAD_REQUEST -> Status.BAD_REQUEST;
      case PRECONDITION_FAILED -> Status.PRECONDITION_FAILED;
      default -> Status.INTERNAL_SERVER_ERROR;
    };
  }
}
