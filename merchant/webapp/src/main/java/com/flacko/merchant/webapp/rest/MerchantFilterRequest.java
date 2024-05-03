package com.flacko.merchant.webapp.rest;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public record MerchantFilterRequest(@RequestParam(OUTGOING_TRAFFIC_STOPPED) Optional<Boolean> outgoingTrafficStopped) {

    private static final String OUTGOING_TRAFFIC_STOPPED = "outgoing_traffic_stopped";

}
