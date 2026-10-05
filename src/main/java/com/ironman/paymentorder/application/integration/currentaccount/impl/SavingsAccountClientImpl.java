package com.ironman.paymentorder.application.integration.currentaccount.impl;

import com.ironman.paymentorder.application.exception.ExceptionCatalog;
import com.ironman.paymentorder.application.integration.currentaccount.CrSavingsAccountFacilityApi;
import com.ironman.paymentorder.application.integration.currentaccount.SavingsAccountClient;
import com.ironman.paymentorder.application.integration.currentaccount.model.SavingsAccountFacility;
import com.ironman.paymentorder.application.integration.currentaccount.restclient.SavingsAccountRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class SavingsAccountClientImpl implements SavingsAccountClient {

  @RestClient private final SavingsAccountRestClient savingsAccountRestClient;

  @Override
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
