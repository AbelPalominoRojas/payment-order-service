package com.ironman.paymentorder.application.integration.partyreference.impl;

import com.ironman.paymentorder.application.exception.ExceptionCatalog;
import com.ironman.paymentorder.application.integration.partyreference.PartyReferenceClient;
import com.ironman.paymentorder.application.integration.partyreference.graphqlclient.PartyReferenceGraphQLClient;
import com.ironman.paymentorder.application.integration.partyreference.model.PartyReferenceRetrieve;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class PartyReferenceClientImpl implements PartyReferenceClient {

  private final PartyReferenceGraphQLClient partyReferenceGraphQLClient;

  @Override
  @Timeout(value = 3, unit = ChronoUnit.SECONDS)
  @CircuitBreaker(
      requestVolumeThreshold = 4,
      failureRatio = 0.5,
      delay = 10,
      delayUnit = ChronoUnit.SECONDS)
  @Retry(
      maxRetries = 2,
      delay = 200,
      delayUnit = ChronoUnit.MILLIS,
      jitter = 100,
      abortOn = CircuitBreakerOpenException.class)
  public PartyReferenceRetrieve retrieve(String documentNumber) {
    try {
      return partyReferenceGraphQLClient.partyReferenceByDocumentNumber(documentNumber);
    } catch (Exception e) {
      log.error(
          "Error retrieving party reference for documentNumber {}: {}",
          documentNumber,
          e.getMessage(),
          e);
      throw ExceptionCatalog.PARTY_REFERENCE_SERVICE.buildException();
    }
  }
}
