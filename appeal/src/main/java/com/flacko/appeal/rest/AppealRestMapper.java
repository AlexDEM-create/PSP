package com.flacko.appeal.rest;

import com.flacko.appeal.Appeal;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class AppealRestMapper {

    AppealResponse mapModelToResponse(Appeal appeal) {
        return new AppealResponse(appeal.getId(),
                appeal.getPaymentId(),
                appeal.getCurrentState(),
                appeal.getCreatedDate().atZone(ZoneId.systemDefault()),
                appeal.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
