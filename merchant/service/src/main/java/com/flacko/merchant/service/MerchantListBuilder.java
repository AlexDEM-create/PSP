package com.flacko.merchant.service;

import java.util.List;

public interface MerchantListBuilder {

    MerchantListBuilder withOutgoingTrafficStopped(boolean outgoingTrafficStopped);

    List<Merchant> build();

}
