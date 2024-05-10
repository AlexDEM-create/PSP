package com.flacko.common.exception;

public class TraderTeamNotFoundException extends NotFoundException {

    public TraderTeamNotFoundException(String id) {
        super(String.format("Trader team %s not found", id));
    }

    public TraderTeamNotFoundException(String entity, String entityId) {
        super(String.format("Trader team not found by %s %s", entity, entityId));
    }

}
