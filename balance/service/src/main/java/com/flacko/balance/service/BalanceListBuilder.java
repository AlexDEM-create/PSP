package com.flacko.balance.service;

import java.util.List;

public interface BalanceListBuilder {

    BalanceListBuilder withEntityId(String entityId);

    BalanceListBuilder withEntityType(EntityType entityType);

    BalanceListBuilder withArchived(Boolean archived);

    List<Balance> build();

}
