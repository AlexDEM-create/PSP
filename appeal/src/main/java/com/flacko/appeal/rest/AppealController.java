package com.flacko.appeal.rest;

import com.flacko.appeal.Appeal;
import com.flacko.appeal.AppealBuilder;
import com.flacko.appeal.AppealService;
import com.flacko.appeal.AppealState;
import com.flacko.appeal.exception.AppealMissingRequiredAttributeException;
import com.flacko.appeal.exception.AppealNotFoundException;
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
    public List<AppealResponse> list() {
        return appealService.list()
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
            throws AppealMissingRequiredAttributeException {
        AppealBuilder builder = appealService.create();
        builder.withPaymentId(appealCreateRequest.paymentId());
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

    @PostMapping("/{appealId}/resolve")
    public AppealResponse resolve(@PathVariable String appealId)
            throws AppealNotFoundException, AppealMissingRequiredAttributeException {
        AppealBuilder builder = appealService.update(appealId);
        builder.withState(AppealState.RESOLVED);
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

    @PostMapping("/{appealId}/reject")
    public AppealResponse reject(@PathVariable String appealId)
            throws AppealNotFoundException, AppealMissingRequiredAttributeException {
        AppealBuilder builder = appealService.update(appealId);
        builder.withState(AppealState.REJECTED);
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

}
