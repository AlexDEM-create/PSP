package com.flacko.trader.team.exception;

import com.flacko.auth.exception.NotFoundException;

public class TraderTeamNotFoundException extends NotFoundException {

    public TraderTeamNotFoundException(String id) {
        super(String.format("Terminal %s not found", id));
    }

}
