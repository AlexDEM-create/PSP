package com.flacko.common.exception;

public class TraderTeamNotFoundException extends NotFoundException {

    public TraderTeamNotFoundException(String id) {
        super(String.format("Terminal %s not found", id));
    }

}
