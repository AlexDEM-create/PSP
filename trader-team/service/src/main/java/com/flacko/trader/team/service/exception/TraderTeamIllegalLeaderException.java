package com.flacko.trader.team.service.exception;

public class TraderTeamIllegalLeaderException extends Exception {

    public TraderTeamIllegalLeaderException(String userId, String traderTeamId) {
        super(String.format("User %s cannot be chosen as leader of trader team %s", userId, traderTeamId));
    }

}
