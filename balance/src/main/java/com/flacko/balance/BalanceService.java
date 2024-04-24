package com.flacko.balance;

import com.flacko.balance.exception.BalanceNotFoundException;

public interface BalanceService {

    BalanceBuilder create();

    BalanceBuilder update(String entityId, EntityType entityType) throws BalanceNotFoundException;

    Balance get(String entityId, EntityType entityType) throws BalanceNotFoundException;

}
