package com.ironman.paymentorder.application.integration.partyreference.model;

import java.time.LocalDateTime;

public record DirectoryEntryDate(
    DirectoryEntryDateType directoryEntryDateType, LocalDateTime directoryEntryDate) {}
