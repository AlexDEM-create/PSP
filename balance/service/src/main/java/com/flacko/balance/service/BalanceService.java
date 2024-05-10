package com.flacko.balance.service;

import com.flacko.common.exception.BalanceNotFoundException;

public interface BalanceService {

    BalanceBuilder create();

    BalanceBuilder update(String entityId, EntityType entityType, BalanceType type) throws BalanceNotFoundException;

    Balance get(String entityId, EntityType entityType, BalanceType type) throws BalanceNotFoundException;

}
