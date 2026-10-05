package com.ironman.paymentorder.application.integration.partyreference.model;

import java.util.List;

public record PartyReferenceRetrieve(
    PartyReference partyReference,
    PartyType partyType,
    List<DirectoryEntryDate> directoryEntryDates) {}
