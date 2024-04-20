
package com.flacko.appeal.rest;

import com.flacko.appeal.Appeal;
import org.springframework.stereotype.Component;

@Component
public class AppealRestMapper {

    AppealResponse mapModelToResponse(Appeal appeal) {
        return new AppealResponse(appeal.getId(),
                appeal.getAppealStatus(),
                appeal.getPaymentId());
    }

}