package com.flacko.common.exception;

public class MerchantInvalidFeeRateException extends Exception {

    public MerchantInvalidFeeRateException(String direction, String id) {
        super(String.format("%s fee rate should be more than 0 for merchant %s", direction, id));
    }

}
