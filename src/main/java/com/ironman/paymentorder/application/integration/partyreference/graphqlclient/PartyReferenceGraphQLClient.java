package com.ironman.paymentorder.application.integration.partyreference.graphqlclient;

import com.ironman.paymentorder.application.integration.partyreference.model.PartyReferenceRetrieve;
import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

@GraphQLClientApi(configKey = "party-reference-service")
public interface PartyReferenceGraphQLClient {

  @Query("partyReference")
  PartyReferenceRetrieve partyReferenceByDocumentNumber(
      @Name("documentNumber") String documentNumber);
}
