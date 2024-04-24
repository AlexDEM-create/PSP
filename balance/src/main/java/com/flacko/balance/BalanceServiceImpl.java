package com.flacko.balance;

import com.flacko.auth.spring.ServiceLocator;
import com.flacko.balance.exception.BalanceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public Balance get(String entityId, EntityType entityType) throws BalanceNotFoundException {
        return balanceRepository.findByEntityIdAndEntityType(entityId, entityType)
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
    public BalanceBuilder update(String entityId, EntityType entityType) throws BalanceNotFoundException {
        Balance existingBalance = get(entityId, entityType);
        return serviceLocator.create(InitializableBalanceBuilder.class)
                .initializeExisting(existingBalance);
    }

}
