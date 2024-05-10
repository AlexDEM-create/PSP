package com.flacko.balance.service;

import com.flacko.common.exception.BalanceNotFoundException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;

import java.util.List;

public interface BalanceService {

    BalanceBuilder create();

    BalanceBuilder update(String entityId, EntityType entityType, BalanceType type) throws BalanceNotFoundException;

    Balance get(String entityId, EntityType entityType, BalanceType type) throws BalanceNotFoundException;

    List<Balance> getMy(String login) throws UserNotFoundException, TraderTeamNotFoundException,
            BalanceNotFoundException, MerchantNotFoundException;

    BalanceListBuilder list();

}
