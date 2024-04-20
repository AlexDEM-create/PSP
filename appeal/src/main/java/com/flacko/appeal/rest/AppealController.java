package com.flacko.appeal.rest;

import com.flacko.appeal.Appeal;
import com.flacko.appeal.AppealBuilder;
import com.flacko.appeal.AppealService;
import com.flacko.appeal.exception.AppealMissingRequiredAttributeException;
import com.flacko.appeal.exception.AppealNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appeals")
public class AppealController {

    private final AppealService appealService;
    private final AppealRestMapper appealRestMapper;

    @PostMapping
    public AppealResponse create(@RequestBody AppealCreateRequest appealCreateRequest)
            throws AppealMissingRequiredAttributeException {
        AppealBuilder builder = appealService.create();
        builder.withPaymentId(appealCreateRequest.paymentId());
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

}
