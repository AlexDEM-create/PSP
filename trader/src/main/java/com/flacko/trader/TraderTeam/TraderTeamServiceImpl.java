package com.flacko.trader.TraderTeam;

import com.flacko.trader.TraderTeam.exception.TraderTeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
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
    private final ApplicationContext context;

    @Override
    public TraderTeamBuilder create() {
        return context.getBean(TraderTeamBuilderImpl.class)
                .initializeNew();
    }

    @Override
    public TraderTeamBuilder update(String id) throws TraderTeamNotFoundException {
        TraderTeam existingTraderTeam = (TraderTeam)get(id);
        return context.getBean(TraderTeamBuilderImpl.class)
                .initializeExisting(existingTraderTeam);
    }

    @Override
    public TraderTeamBuilder get(String id) throws TraderTeamNotFoundException {
        return context.getBean(TraderTeamBuilderImpl.class)
                .initializeExisting(traderTeamRepository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new TraderTeamNotFoundException(id)));
    }

    @Override
    public List<TraderTeamBuilder> list() {
        return StreamSupport.stream(traderTeamRepository.findAll().spliterator(), false)
                .map(traderTeam -> context.getBean(TraderTeamBuilderImpl.class)
                        .initializeExisting(traderTeam))
                .collect(Collectors.toList());
    }
}
