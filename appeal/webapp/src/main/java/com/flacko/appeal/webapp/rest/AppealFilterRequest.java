package com.flacko.appeal.webapp.rest;

import com.flacko.appeal.service.AppealSource;
import com.flacko.appeal.service.AppealState;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public record AppealFilterRequest(@RequestParam(PAYMENT_ID) Optional<String> paymentId,
                                  @RequestParam(SOURCE) Optional<AppealSource> source,
                                  @RequestParam(CURRENT_STATE) Optional<AppealState> currentState) {

    private static final String PAYMENT_ID = "payment_id";
    private static final String SOURCE = "source";
    private static final String CURRENT_STATE = "current_state";

}
