package com.ironman.paymentorder.application.integration.partyreference.model;

public record PartyIdentification(
    PartyIdentificationType partyIdentificationType, Identifier partyIdentification) {}
