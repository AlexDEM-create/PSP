package com.flacko.trader.exception;

public class TraderNotFoundException extends Exception {
    public TraderNotFoundException(String id) {
        super(String.format("Trader %s not found", id));
    }
}