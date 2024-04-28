package com.flacko.appeal.impl;

import com.flacko.appeal.service.Appeal;
import com.flacko.appeal.service.AppealBuilder;

public interface InitializableAppealBuilder extends AppealBuilder {

    AppealBuilder initializeNew();

    AppealBuilder initializeExisting(Appeal existingAppeal);

}
