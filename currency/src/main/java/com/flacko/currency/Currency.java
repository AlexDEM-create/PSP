package com.flacko.currency;

public interface Currency {

    Long getPrimaryKey();

    String getId();

    String getTradeType();

    String getRate();

    String getFiat();


}