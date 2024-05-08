package com.flacko.appeal.webapp.rest;

import com.flacko.appeal.service.*;
import com.flacko.appeal.service.exception.AppealIllegalPaymentCurrentStateException;
import com.flacko.appeal.service.exception.AppealIllegalStateTransitionException;
import com.flacko.appeal.service.exception.AppealMissingRequiredAttributeException;
import com.flacko.common.exception.AppealNotFoundException;
import com.flacko.common.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appeals")
public class AppealController {

    private static final String PAYMENT_ID = "payment_id";
    private static final String SOURCE = "source";
    private static final String CURRENT_STATE = "current_state";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final AppealService appealService;
    private final AppealRestMapper appealRestMapper;

    @GetMapping
    public List<AppealResponse> list(@RequestParam(PAYMENT_ID) Optional<String> paymentId,
                                     @RequestParam(SOURCE) Optional<AppealSource> source,
                                     @RequestParam(CURRENT_STATE) Optional<AppealState> currentState,
                                     @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                     @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        AppealListBuilder builder = appealService.list();
        paymentId.ifPresent(builder::withPaymentId);
        source.ifPresent(builder::withSource);
        currentState.ifPresent(builder::withCurrentState);

        List<AppealResponse> appeals = builder.build()
                .stream()
                .map(appealRestMapper::mapModelToResponse)
                .filter(appeal -> appeal.currentState() == AppealState.INITIATED)
                .sorted(Comparator.comparing(AppealResponse::createdDate).reversed())
                .collect(Collectors.toList());

        appeals.addAll(builder.build()
                .stream()
                .map(appealRestMapper::mapModelToResponse)
                .filter(appeal -> appeal.currentState() != AppealState.INITIATED)
                .sorted(Comparator.comparing(AppealResponse::createdDate).reversed())
                .collect(Collectors.toList()));

        return appeals.stream()
                .skip(offset)
                .limit(limit)
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

    @PatchMapping("/{appealId}/resolve")
    public AppealResponse resolve(@PathVariable String appealId)
            throws AppealNotFoundException, AppealMissingRequiredAttributeException,
            AppealIllegalStateTransitionException, PaymentNotFoundException, AppealIllegalPaymentCurrentStateException {
        AppealBuilder builder = appealService.update(appealId);
        builder.withState(AppealState.RESOLVED);
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

    @PatchMapping("/{appealId}/reject")
    public AppealResponse reject(@PathVariable String appealId)
            throws AppealNotFoundException, AppealMissingRequiredAttributeException,
            AppealIllegalStateTransitionException, PaymentNotFoundException, AppealIllegalPaymentCurrentStateException {
        AppealBuilder builder = appealService.update(appealId);
        builder.withState(AppealState.REJECTED);
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

    @PatchMapping("/{appealId}/review")
    public AppealResponse review(@PathVariable String appealId)
            throws AppealNotFoundException, AppealMissingRequiredAttributeException,
            AppealIllegalStateTransitionException, PaymentNotFoundException, AppealIllegalPaymentCurrentStateException {
        AppealBuilder builder = appealService.update(appealId);
        builder.withState(AppealState.UNDER_REVIEW);
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

}
