package com.flacko.appeal.service;

import java.util.List;

public interface AppealListBuilder {

    AppealListBuilder withPaymentId(String paymentId);

    AppealListBuilder withSource(AppealSource source);

    AppealListBuilder withCurrentState(AppealState currentState);

    List<Appeal> build();

}
