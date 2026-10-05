package com.ironman.paymentorder.application.integration.currentaccount;

import com.ironman.paymentorder.application.integration.currentaccount.model.SavingsAccountFacility;

public interface SavingsAccountClient {
  SavingsAccountFacility retrieve(String savingsAccountId);
}
