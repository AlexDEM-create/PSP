package com.flacko.balance.impl;

import com.flacko.balance.service.*;
import com.flacko.common.exception.BalanceNotFoundException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.common.role.UserRole;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.merchant.service.MerchantService;
import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.user.service.User;
import com.flacko.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final ServiceLocator serviceLocator;
    private final UserService userService;
    private final MerchantService merchantService;
    private final TraderTeamService traderTeamService;

    @Override
    public BalanceListBuilder list() {
        return serviceLocator.create(BalanceListBuilder.class);
    }

    @Override
    public Balance get(String entityId, EntityType entityType, BalanceType type) throws BalanceNotFoundException {
        return balanceRepository.findByEntityIdAndEntityTypeAndType(entityId, entityType, type)
                .orElseThrow(() -> new BalanceNotFoundException(entityId));
    }

    @Override
    public List<Balance> getMy(String login) throws BalanceNotFoundException, UserNotFoundException,
            TraderTeamNotFoundException, MerchantNotFoundException {
        User user = userService.getByLogin(login);
        String entityId;
        EntityType entityType;
        if (user.getRole() == UserRole.MERCHANT) {
            entityId = merchantService.getByUserId(user.getId())
                    .getId();
            entityType = EntityType.MERCHANT;
        } else if (user.getRole() == UserRole.TRADER_TEAM || user.getRole() == UserRole.TRADER_TEAM_LEADER) {
            entityId = traderTeamService.getByUserId(user.getId())
                    .getId();
            entityType = EntityType.TRADER_TEAM;
        } else {
            throw new BalanceNotFoundException(user.getRole(), user.getId());
        }
        return list().withEntityId(entityId)
                .withEntityType(entityType)
                .build();
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
