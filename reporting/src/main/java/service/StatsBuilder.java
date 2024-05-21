package service;

import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import service.EntityType;
import service.exception.StatsMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface StatsBuilder {

    StatsBuilder withEntityId(String entityId);

    StatsBuilder withEntityType(EntityType entityType);

    StatsBuilder withTodayOutgoingTotal(BigDecimal amount);

    StatsBuilder withTodayIncomingTotal(BigDecimal amount);

    StatsBuilder withAllTimeOutgoingTotal(BigDecimal amount);

    StatsBuilder withAllTimeIncomingTotal(BigDecimal amount);

    Stats build() throws StatsMissingRequiredAttributeException, MerchantNotFoundException,
            TraderTeamNotFoundException;

}
