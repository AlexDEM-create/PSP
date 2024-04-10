package com.flacko.merchant.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MerchantInitiateRequest(@JsonProperty(NAME) String name) {
    private static final String NAME = "name";
}
