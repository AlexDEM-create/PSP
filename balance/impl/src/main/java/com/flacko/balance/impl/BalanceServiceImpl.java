package com.flacko.balance.impl;

import com.flacko.balance.service.*;
import com.flacko.common.exception.BalanceNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public Balance get(String entityId, EntityType entityType, BalanceType type) throws BalanceNotFoundException {
        return balanceRepository.findByEntityIdAndEntityTypeAndType(entityId, entityType, type)
                .orElseThrow(() -> new BalanceNotFoundException(entityId));
    }

    @Override
    @Transactional
    public BalanceBuilder create() {
        return serviceLocator.create(InitializableBalanceBuilder.class)
                .initializeNew();
    }

    @Override
    @Transactional
    public BalanceBuilder update(String entityId, EntityType entityType, BalanceType type)
            throws BalanceNotFoundException {
        Balance existingBalance = get(entityId, entityType, type);
        return serviceLocator.create(InitializableBalanceBuilder.class)
                .initializeExisting(existingBalance);
    }

}
