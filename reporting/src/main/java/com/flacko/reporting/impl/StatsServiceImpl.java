package com.flacko.reporting.impl;

import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.service.incoming.IncomingPayment;
import com.flacko.payment.service.incoming.IncomingPaymentService;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamService;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flacko.reporting.service.EntityType;
import com.flacko.reporting.service.Stats;
import com.flacko.reporting.service.StatsBuilder;
import com.flacko.reporting.service.StatsService;
import com.flacko.reporting.service.exception.StatsMissingRequiredAttributeException;
import com.flacko.reporting.service.exception.StatsNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    private final ServiceLocator serviceLocator;
    private final MerchantService merchantService;
    private final TraderTeamService traderTeamService;
    private final OutgoingPaymentService outgoingPaymentService;
    private final IncomingPaymentService incomingPaymentService;


    @Override
    public Stats get(String entityId, EntityType entityType) throws StatsNotFoundException {
        return statsRepository.findByEntityIdAndEntityType(entityId, entityType)
                .orElseThrow(() -> new StatsNotFoundException(entityId));
    }

    @Transactional
    @Override
    public StatsBuilder create() {
        return serviceLocator.create(InitializableStatsBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public StatsBuilder update(String entityId, EntityType entityType) throws StatsNotFoundException {
        Stats existingStats = get(entityId, entityType);
        return serviceLocator.create(InitializableStatsBuilder.class)
                .initializeExisting(existingStats);
    }

    @SuppressWarnings("unused")
    @Scheduled(fixedRate = 600000)
    public void updateStats() throws StatsNotFoundException, TraderTeamNotFoundException, StatsMissingRequiredAttributeException, MerchantNotFoundException {
        // Получить список всех существующих EntityType
        List<EntityType> entityTypes = List.of(EntityType.values());

        // Для каждого типа сущности
        for (EntityType entityType : entityTypes) {
            // Получить список всех ID сущностей этого типа
            List<String> entityIds = getEntityIds(entityType);

            // Для каждого ID сущности
            for (String entityId : entityIds) {
                // Получить текущую статистику
                Stats existingStats;
                try {
                    existingStats = get(entityId, entityType);
                } catch (StatsNotFoundException e) {
                    // Если статистики не существует, создать новую
                    existingStats = create().withEntityId(entityId).withEntityType(entityType).build();
                }

                // Вычислить новую статистику
                BigDecimal newOutgoingTotal = calculateOutgoingTotal(entityId);
                BigDecimal newIncomingTotal = calculateIncomingTotal(entityId);
                BigDecimal newAllTimeOutgoingTotal = calculateAllTimeOutgoingTotal(entityId);
                BigDecimal newAllTimeIncomingTotal = calculateAllTimeIncomingTotal(entityId);

                // Обновить статистику
                update(entityId, entityType)
                        .withTodayOutgoingTotal(newOutgoingTotal)
                        .withTodayIncomingTotal(newIncomingTotal)
                        .withAllTimeOutgoingTotal(newAllTimeOutgoingTotal)
                        .withAllTimeIncomingTotal(newAllTimeIncomingTotal)
                        .build();
            }
        }
    }

    private List<String> getEntityIds(EntityType entityType) {
        switch (entityType) {
            case MERCHANT:
                List<Merchant> merchants = merchantService.list().build();
                return merchants.stream().map(Merchant::getId).collect(Collectors.toList());
            case TRADER_TEAM:
                List<TraderTeam> traderTeams = traderTeamService.list().build();
                return traderTeams.stream().map(TraderTeam::getId).collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Unsupported entity type: " + entityType);
        }
    }
    private BigDecimal calculateOutgoingTotal(String entityId) {
        List<OutgoingPayment> outgoingPayments = outgoingPaymentService.list().build();
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime now = LocalDateTime.now();
        BigDecimal total = outgoingPayments.stream()
                .filter(payment -> payment.getId().equals(entityId))
                .filter(payment -> payment.getCurrentState().equals(PaymentState.VERIFIED))
                .filter(payment -> LocalDateTime.from(payment.getCreatedDate()).isAfter(startOfDay)
                        && LocalDateTime.from(payment.getCreatedDate()).isBefore(now))
                .map(OutgoingPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }
    private BigDecimal calculateIncomingTotal(String entityId) {
        List<IncomingPayment> incomingPayments =  incomingPaymentService.list().build();
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime now = LocalDateTime.now();
        BigDecimal total = incomingPayments.stream()
                .filter(payment -> payment.getId().equals(entityId))
                .filter(payment -> payment.getCurrentState().equals(PaymentState.VERIFIED))
                .filter(payment -> LocalDateTime.from(payment.getCreatedDate()).isAfter(startOfDay)
                        && LocalDateTime.from(payment.getCreatedDate()).isBefore(now))
                .map(IncomingPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    private BigDecimal calculateAllTimeOutgoingTotal(String entityId) {
        List<OutgoingPayment> allTimeOutgoingPayments = outgoingPaymentService.list().build();
        BigDecimal total = allTimeOutgoingPayments.stream()
                .filter(payment -> payment.getId().equals(entityId))
                .filter(payment -> payment.getCurrentState().equals(PaymentState.VERIFIED))
                .map(OutgoingPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    private BigDecimal calculateAllTimeIncomingTotal(String entityId) {
        List<IncomingPayment> allTimeIncomingPayments = incomingPaymentService.list().build();
        BigDecimal total = allTimeIncomingPayments.stream()
                .filter(payment -> payment.getId().equals(entityId))
                .filter(payment -> payment.getCurrentState().equals(PaymentState.VERIFIED))
                .map(IncomingPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }



}