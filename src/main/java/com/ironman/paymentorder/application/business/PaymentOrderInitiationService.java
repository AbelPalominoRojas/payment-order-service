package com.ironman.paymentorder.application.business;

import com.ironman.paymentorder.application.model.api.PaymentOrderInitiationTransaction;
import com.ironman.paymentorder.application.model.api.PaymentTransactionResponse;

public interface PaymentOrderInitiationService {

  PaymentTransactionResponse initiate(
      PaymentOrderInitiationTransaction paymentOrderInitiationTransaction);
}
