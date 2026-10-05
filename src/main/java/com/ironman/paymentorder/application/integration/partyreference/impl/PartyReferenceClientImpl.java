package com.ironman.paymentorder.application.integration.partyreference.impl;

import com.ironman.paymentorder.application.exception.ExceptionCatalog;
import com.ironman.paymentorder.application.integration.partyreference.PartyReferenceClient;
import com.ironman.paymentorder.application.integration.partyreference.graphqlclient.PartyReferenceGraphQLClient;
import com.ironman.paymentorder.application.integration.partyreference.model.PartyReferenceRetrieve;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class PartyReferenceClientImpl implements PartyReferenceClient {

  private final PartyReferenceGraphQLClient partyReferenceGraphQLClient;

  @Override
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
