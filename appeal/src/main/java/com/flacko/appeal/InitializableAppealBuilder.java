
package com.flacko.appeal;

public interface InitializableAppealBuilder extends AppealBuilder {

    AppealBuilder initializeNew();

    AppealBuilder initializeExisting(Appeal existingAppeal);

    AppealBuilder withAppealStatus(AppealStatus appealStatus);

}
