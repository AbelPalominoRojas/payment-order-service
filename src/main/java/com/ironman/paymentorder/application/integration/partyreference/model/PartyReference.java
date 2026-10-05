package com.ironman.paymentorder.application.integration.partyreference.model;

import java.util.List;

public record PartyReference(
    String partyId, PartyIdentification partyIdentification, List<PartyName> partyNames) {}
