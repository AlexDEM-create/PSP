package com.flacko.trader.team.service.exception;

public class TraderTeamInvalidFeeRateException extends Exception {

    public TraderTeamInvalidFeeRateException(String attributeName, String id) {
        super(String.format("Fee rate %s cannot be less than 0 for trader team %s", attributeName, id));
    }

}
