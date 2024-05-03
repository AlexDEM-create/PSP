package com.flacko.appeal.webapp.rest;

import com.flacko.appeal.service.*;
import com.flacko.appeal.service.exception.AppealIllegalPaymentCurrentStateException;
import com.flacko.appeal.service.exception.AppealIllegalStateTransitionException;
import com.flacko.appeal.service.exception.AppealMissingRequiredAttributeException;
import com.flacko.common.exception.AppealNotFoundException;
import com.flacko.common.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appeals")
public class AppealController {

    private final AppealService appealService;
    private final AppealRestMapper appealRestMapper;

    @GetMapping
    public List<AppealResponse> list(AppealFilterRequest appealFilterRequest) {
        AppealListBuilder builder = appealService.list();
        appealFilterRequest.paymentId().ifPresent(builder::withPaymentId);
        appealFilterRequest.source().ifPresent(builder::withSource);
        appealFilterRequest.currentState().ifPresent(builder::withCurrentState);
        return builder.build()
                .stream()
                .map(appealRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{appealId}")
    public AppealResponse get(@PathVariable String appealId) throws AppealNotFoundException {
        return appealRestMapper.mapModelToResponse(appealService.get(appealId));
    }

    @PostMapping
    public AppealResponse create(@RequestBody AppealCreateRequest appealCreateRequest)
            throws AppealMissingRequiredAttributeException, PaymentNotFoundException,
            AppealIllegalPaymentCurrentStateException {
        AppealBuilder builder = appealService.create();
        builder.withPaymentId(appealCreateRequest.paymentId())
                .withSource(appealCreateRequest.source());
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

    @PostMapping("/{appealId}/resolve")
    public AppealResponse resolve(@PathVariable String appealId)
            throws AppealNotFoundException, AppealMissingRequiredAttributeException,
            AppealIllegalStateTransitionException, PaymentNotFoundException, AppealIllegalPaymentCurrentStateException {
        AppealBuilder builder = appealService.update(appealId);
        builder.withState(AppealState.RESOLVED);
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

    @PostMapping("/{appealId}/reject")
    public AppealResponse reject(@PathVariable String appealId)
            throws AppealNotFoundException, AppealMissingRequiredAttributeException,
            AppealIllegalStateTransitionException, PaymentNotFoundException, AppealIllegalPaymentCurrentStateException {
        AppealBuilder builder = appealService.update(appealId);
        builder.withState(AppealState.REJECTED);
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

}
