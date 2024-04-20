package com.flacko.card;

import java.time.Instant;
import java.util.Optional;

public interface Card {
    String getCardId();
    String getCardNumber();
    String getCardName();
    Instant getCardDate();
    String getBankId();
    boolean isActive();
    Optional<String> getTraderId();
    Instant getCreatedDate();
    Instant getUpdatedDate();
    Optional<Instant> getDeletedDate();
}
