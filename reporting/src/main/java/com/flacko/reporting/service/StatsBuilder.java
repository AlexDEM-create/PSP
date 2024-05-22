package com.flacko.reporting.service;

import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.reporting.service.exception.StatsMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface StatsBuilder {

    StatsBuilder withEntityId(String entityId);

    StatsBuilder withEntityType(EntityType entityType);

    StatsBuilder withTodayOutgoingTotal(BigDecimal todayOutgoingTotal);

    StatsBuilder withTodayIncomingTotal(BigDecimal todayIncomingTotal);

    StatsBuilder withAllTimeOutgoingTotal(BigDecimal allTimeOutgoingTotal);

    StatsBuilder withAllTimeIncomingTotal(BigDecimal allTimeIncomingTotal);

    Stats build() throws StatsMissingRequiredAttributeException, MerchantNotFoundException,
            TraderTeamNotFoundException;

}
