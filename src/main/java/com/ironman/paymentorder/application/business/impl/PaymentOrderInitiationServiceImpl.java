package com.ironman.paymentorder.application.business.impl;

import com.ironman.paymentorder.application.business.PaymentOrderInitiationService;
import com.ironman.paymentorder.application.exception.ApplicationException;
import com.ironman.paymentorder.application.exception.ExceptionCatalog;
import com.ironman.paymentorder.application.kafka.PaymentOrderInitiationProducer;
import com.ironman.paymentorder.application.model.api.*;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class PaymentOrderInitiationServiceImpl implements PaymentOrderInitiationService {
  private final PaymentOrderInitiationProducer paymentOrderInitiationProducer;

  @Override
  public PaymentTransactionResponse initiate(
      PaymentOrderInitiationTransaction paymentOrderInitiationTransaction) {
    try {
      var correlationId = paymentOrderInitiationProducer.publish(paymentOrderInitiationTransaction);

      return buildPaymentTransactionResponse(correlationId);

    } catch (ApplicationException e) {
      log.error("Failed to publish payment order initiation event AppEx: {}", e.getMessage(), e);
      throw e;
    } catch (Exception e) {
      log.error("Failed to publish payment order initiation event EX: {}", e.getMessage(), e);
      throw ExceptionCatalog.APPLICATION_ERROR.buildException();
    }
  }

  private static PaymentTransactionResponse buildPaymentTransactionResponse(String correlationId) {
    var paymentIdentification = new Identifier().identifierValue(correlationId);
    var paymentTransactionIdentification =
        new PaymentIdentification()
            .paymentIdentification(paymentIdentification)
            .paymentIdentificationType(PaymentIdentificationTypeValues.PAYMENT_ORDER_ID);

    var paymentTransactionStatus =
        new PaymentTransactionStatus()
            .paymentTransactionStatusType(PaymentTransactionStatusTypeValues.RECEIVED);

    return new PaymentTransactionResponse()
        .paymentTransactionIdentification(paymentTransactionIdentification)
        .paymentTransactionStatus(paymentTransactionStatus);
  }
}
