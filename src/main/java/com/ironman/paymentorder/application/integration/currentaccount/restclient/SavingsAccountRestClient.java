package com.ironman.paymentorder.application.integration.currentaccount.restclient;

import com.ironman.paymentorder.application.integration.currentaccount.CrSavingsAccountFacilityApi;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "current-account-service")
public interface SavingsAccountRestClient extends CrSavingsAccountFacilityApi {}
