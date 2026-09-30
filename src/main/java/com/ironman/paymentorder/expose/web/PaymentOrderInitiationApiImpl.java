package com.ironman.paymentorder.expose.web;

import com.ironman.paymentorder.application.business.PaymentOrderInitiationService;
import com.ironman.paymentorder.application.model.api.PaymentOrderInitiationTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class PaymentOrderInitiationApiImpl implements PaymentOrderInitiationApi {
  private final PaymentOrderInitiationService paymentOrderInitiationService;

  @Override
  public Response initiate(PaymentOrderInitiationTransaction paymentOrderInitiationTransaction) {
    var response = paymentOrderInitiationService.initiate(paymentOrderInitiationTransaction);
    return Response.status(Response.Status.CREATED).entity(response).build();
  }
}
