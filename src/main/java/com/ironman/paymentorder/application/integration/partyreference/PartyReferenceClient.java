package com.ironman.paymentorder.application.integration.partyreference;

import com.ironman.paymentorder.application.integration.partyreference.model.PartyReferenceRetrieve;

public interface PartyReferenceClient {
  PartyReferenceRetrieve retrieve(String documentNumber);
}
