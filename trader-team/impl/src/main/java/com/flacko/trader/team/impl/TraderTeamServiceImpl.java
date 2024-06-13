package com.flacko.trader.team.impl;

import com.flacko.common.exception.NoEligibleTraderTeamsException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamBuilder;
import com.flacko.trader.team.service.TraderTeamListBuilder;
import com.flacko.trader.team.service.TraderTeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraderTeamServiceImpl implements TraderTeamService {

    private final TraderTeamRepository traderTeamRepository;
    private final ServiceLocator serviceLocator;
    private final PaymentMethodService paymentMethodService;

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

    @Override
    public TraderTeam getByLeaderId(String leaderId) throws TraderTeamNotFoundException {
        return traderTeamRepository.findByLeaderId(leaderId)
                .orElseThrow(() -> new TraderTeamNotFoundException("leader", leaderId));
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

    @Override
    public TraderTeam getRandomEligibleTraderTeamForOutgoingPayment(Optional<String> currentTraderTeamId)
            throws NoEligibleTraderTeamsException, TraderTeamNotFoundException {
        List<TraderTeam> eligibleTeams = list()
                .withVerified(true)
                .withOutgoingOnline(true)
                .withKickedOut(false)
                .withArchived(false)
                .build()
                .stream()
                .filter(this::hasEnabledPaymentMethods)
                .filter(traderTeam -> currentTraderTeamId.isEmpty()
                        || !traderTeam.getId().equals(currentTraderTeamId.get()))
                .toList();

        if (eligibleTeams.isEmpty()) {
            if (currentTraderTeamId.isPresent()) {
                log.error("random eligible team didn't change " + currentTraderTeamId.get());
                return get(currentTraderTeamId.get());
            }
            throw new NoEligibleTraderTeamsException();
        }
        SecureRandom random = new SecureRandom();
        int randomIndex = random.nextInt(eligibleTeams.size());
        log.error("eligible teams " + eligibleTeams + ", random index " + randomIndex);
        return eligibleTeams.get(randomIndex);
    }

    private boolean hasEnabledPaymentMethods(TraderTeam traderTeam) {
        return !paymentMethodService.list()
                .withTraderTeamId(traderTeam.getId())
                .withEnabled(true)
                .build()
                .isEmpty();
    }

}
