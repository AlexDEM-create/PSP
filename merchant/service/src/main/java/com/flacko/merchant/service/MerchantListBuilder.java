package com.flacko.merchant.service;

import com.flacko.common.country.Country;

import java.util.List;

public interface MerchantListBuilder {

    MerchantListBuilder withCountry(Country country);

    MerchantListBuilder withOutgoingTrafficStopped(boolean outgoingTrafficStopped);

    MerchantListBuilder withArchived(Boolean archived);

    List<Merchant> build();

}
