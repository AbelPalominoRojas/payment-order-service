package com.ironman.paymentorder.expose.web;

import com.ironman.paymentorder.application.model.api.PaymentOrderInitiationTransaction;
import jakarta.ws.rs.core.Response;

public class PaymentOrderInitiationApiImpl implements PaymentOrderInitiationApi {
  @Override
  public Response initiate(PaymentOrderInitiationTransaction paymentOrderInitiationTransaction) {
    return Response.status(Response.Status.CREATED)
        .entity(paymentOrderInitiationTransaction)
        .build();
  }
}
