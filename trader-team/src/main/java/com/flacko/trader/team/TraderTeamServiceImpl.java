package com.flacko.trader.team;

import com.flacko.auth.spring.ServiceLocator;
import com.flacko.trader.team.exception.TraderTeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class TraderTeamServiceImpl implements TraderTeamService {

    private final TraderTeamRepository traderTeamRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public List<TraderTeam> list() {
        return StreamSupport.stream(traderTeamRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public TraderTeam get(String id) throws TraderTeamNotFoundException {
        return traderTeamRepository.findById(id)
                .orElseThrow(() -> new TraderTeamNotFoundException(id));
    }

    @Override
    public TraderTeamBuilder create() {
        return serviceLocator.create(InitializableTraderTeamBuilder.class)
                .initializeNew();
    }

    @Override
    public TraderTeamBuilder update(String id) throws TraderTeamNotFoundException {
        TraderTeam existingTraderTeam = get(id);
        return serviceLocator.create(InitializableTraderTeamBuilder.class)
                .initializeExisting(existingTraderTeam);
    }

}
