package com.flacko.common.exception;

public class BalanceInvalidCurrentBalanceException extends Exception {

    public BalanceInvalidCurrentBalanceException(String balanceType, String id, String entityType, String entityId) {
        super(String.format("%s balance %s cannot be less than 0 for entity type %s, entity ID %s",
                balanceType, id, entityType, entityId));
    }

}
