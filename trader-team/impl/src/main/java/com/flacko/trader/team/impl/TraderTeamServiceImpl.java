package com.flacko.trader.team.impl;

import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamBuilder;
import com.flacko.trader.team.service.TraderTeamListBuilder;
import com.flacko.trader.team.service.TraderTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TraderTeamServiceImpl implements TraderTeamService {

    private final TraderTeamRepository traderTeamRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public TraderTeamListBuilder list() {
        return serviceLocator.create(TraderTeamListBuilder.class);
    }

    @Override
    public TraderTeam get(String id) throws TraderTeamNotFoundException {
        return traderTeamRepository.findById(id)
                .orElseThrow(() -> new TraderTeamNotFoundException(id));
    }

    @Override
    public TraderTeam getByUserId(String userId) throws TraderTeamNotFoundException {
        return traderTeamRepository.findByUserId(userId)
                .orElseThrow(() -> new TraderTeamNotFoundException("user", userId));
    }

    @Transactional
    @Override
    public TraderTeamBuilder create() {
        return serviceLocator.create(InitializableTraderTeamBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public TraderTeamBuilder update(String id) throws TraderTeamNotFoundException {
        TraderTeam existingTraderTeam = get(id);
        return serviceLocator.create(InitializableTraderTeamBuilder.class)
                .initializeExisting(existingTraderTeam);
    }

}
