package com.ironman.paymentorder.application.business.impl;

import com.ironman.paymentorder.application.business.PaymentOrderInitiationService;
import com.ironman.paymentorder.application.exception.ApplicationException;
import com.ironman.paymentorder.application.exception.ExceptionCatalog;
import com.ironman.paymentorder.application.integration.currentaccount.SavingsAccountClient;
import com.ironman.paymentorder.application.integration.partyreference.PartyReferenceClient;
import com.ironman.paymentorder.application.kafka.PaymentOrderInitiationProducer;
import com.ironman.paymentorder.application.model.api.*;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class PaymentOrderInitiationServiceImpl implements PaymentOrderInitiationService {
  private final PaymentOrderInitiationProducer paymentOrderInitiationProducer;
  private final SavingsAccountClient savingsAccountClient;
  private final PartyReferenceClient partyReferenceClient;
  private final ManagedExecutor managedExecutor;

  @Override
  public PaymentTransactionResponse initiate(
      PaymentOrderInitiationTransaction paymentOrderInitiationTransaction) {
    try {
      var accountId = getAccountId(paymentOrderInitiationTransaction.getPaymentTransaction());
      var documentNumber = getDocumentNumber(paymentOrderInitiationTransaction.getPayerReference());

      var accountUni = offloadBlocking(() -> savingsAccountClient.retrieve(accountId));
      var partyReferenceUni = offloadBlocking(() -> partyReferenceClient.retrieve(documentNumber));

      var results =
          Uni.combine().all().unis(accountUni, partyReferenceUni).asTuple().await().indefinitely();

      ensurePresent(
          results.getItem1(),
          ExceptionCatalog.CURRENT_ACCOUNT_EMPTY,
          "Current account service returned an empty response for accountId: {}",
          accountId);

      ensurePresent(
          results.getItem2(),
          ExceptionCatalog.PARTY_REFERENCE_EMPTY,
          "Party reference service returned an empty response for documentNumber: {}",
          documentNumber);

      var correlationId = paymentOrderInitiationProducer.publish(paymentOrderInitiationTransaction);

      return buildPaymentTransactionResponse(correlationId);

    } catch (ApplicationException e) {
      log.error("Failed to initiate payment order AppEx: {}", e.getMessage(), e);
      throw e;
    } catch (TimeoutException | CircuitBreakerOpenException e) {
      log.error(
          "Downstream service unavailable while initiating payment order: {}", e.getMessage(), e);
      throw ExceptionCatalog.DOWNSTREAM_UNAVAILABLE.buildException();
    } catch (Exception e) {
      log.error("Failed to initiate payment order EX: {}", e.getMessage(), e);
      throw ExceptionCatalog.APPLICATION_ERROR.buildException();
    }
  }

  private <T> Uni<T> offloadBlocking(Supplier<T> blockingCall) {
    return Uni.createFrom().item(blockingCall).runSubscriptionOn(managedExecutor);
  }

  private static <T> void ensurePresent(
      T value, ExceptionCatalog emptyException, String warnMessage, Object arg) {
    if (value == null) {
      log.warn(warnMessage, arg);
      throw emptyException.buildException();
    }
  }

  private static String getAccountId(PaymentTransaction paymentTransaction) {
    return paymentTransaction
        .getFinancialTransactionFromAccount()
        .getAccountIdentification()
        .getAccountIdentification()
        .getIdentifierValue();
  }

  private static String getDocumentNumber(Payer payerReference) {
    return payerReference.getPartyIdentification().getPartyIdentification().getIdentifierValue();
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
