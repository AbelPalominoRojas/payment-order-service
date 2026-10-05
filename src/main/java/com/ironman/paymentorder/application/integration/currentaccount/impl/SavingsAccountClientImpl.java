package com.ironman.paymentorder.application.integration.currentaccount.impl;

import com.ironman.paymentorder.application.exception.ExceptionCatalog;
import com.ironman.paymentorder.application.integration.currentaccount.CrSavingsAccountFacilityApi;
import com.ironman.paymentorder.application.integration.currentaccount.SavingsAccountClient;
import com.ironman.paymentorder.application.integration.currentaccount.model.SavingsAccountFacility;
import com.ironman.paymentorder.application.integration.currentaccount.restclient.SavingsAccountRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class SavingsAccountClientImpl implements SavingsAccountClient {

  @RestClient private final SavingsAccountRestClient savingsAccountRestClient;

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
  public SavingsAccountFacility retrieve(String savingsAccountId) {

    try {
      CrSavingsAccountFacilityApi.RetrieveRequest request =
          CrSavingsAccountFacilityApi.RetrieveRequest.newInstance()
              .savingsAccountId(savingsAccountId);

      return savingsAccountRestClient.retrieve(request);
    } catch (Exception e) {
      log.error(
          "Error retrieving savings account facility with ID {}: {}",
          savingsAccountId,
          e.getMessage(),
          e);
      throw ExceptionCatalog.CURRENT_ACCOUNT_SERVICE.buildException();
    }
  }
}
