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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.ZoneId;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(StatsServiceImpl.class);


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
    @Scheduled(fixedRate = 10000)
    public void updateStats() throws StatsNotFoundException, TraderTeamNotFoundException, StatsMissingRequiredAttributeException, MerchantNotFoundException {
        LOGGER.info("updateStats: begin");
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
                BigDecimal newOutgoingTotal = calculateTodayOutgoingTotal(entityId, entityType);
                BigDecimal newIncomingTotal = calculateIncomingTotal(entityId, entityType);
                BigDecimal newAllTimeOutgoingTotal = calculateAllTimeOutgoingTotal(entityId, entityType);
                BigDecimal newAllTimeIncomingTotal = calculateAllTimeIncomingTotal(entityId, entityType);

                // Обновить статистику
                update(entityId, entityType)
                        .withTodayOutgoingTotal(newOutgoingTotal)
                        .withTodayIncomingTotal(newIncomingTotal)
                        .withAllTimeOutgoingTotal(newAllTimeOutgoingTotal)
                        .withAllTimeIncomingTotal(newAllTimeIncomingTotal)
                        .build();
            }
        }
        LOGGER.info("updateStats: end");
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
    private BigDecimal calculateTodayOutgoingTotal(String entityId, EntityType entityType) {
        List<OutgoingPayment> outgoingPayments = outgoingPaymentService.list().build();
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime now = LocalDateTime.now();
        BigDecimal total = outgoingPayments.stream()
                .filter(payment -> (entityType == EntityType.MERCHANT && payment.getMerchantId().equals(entityId))
                        || (entityType == EntityType.TRADER_TEAM && payment.getTraderTeamId().equals(entityId)))
                .filter(payment -> payment.getCurrentState().equals(PaymentState.VERIFIED))
                .filter(payment -> payment.getCreatedDate().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(startOfDay)
                        && payment.getCreatedDate().atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(now))
                .map(OutgoingPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }
    private BigDecimal calculateIncomingTotal(String entityId, EntityType entityType) {
        List<IncomingPayment> incomingPayments = incomingPaymentService.list().build();
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime now = LocalDateTime.now();
        BigDecimal total = incomingPayments.stream()
                .filter(payment -> (entityType == EntityType.MERCHANT && payment.getMerchantId().equals(entityId))
                        || (entityType == EntityType.TRADER_TEAM && payment.getTraderTeamId().equals(entityId)))
                .filter(payment -> payment.getCurrentState().equals(PaymentState.VERIFIED))
                .filter(payment -> payment.getCreatedDate().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(startOfDay)
                        && payment.getCreatedDate().atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(now))
                .map(IncomingPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    private BigDecimal calculateAllTimeOutgoingTotal(String entityId, EntityType entityType) {
        List<OutgoingPayment> allTimeOutgoingPayments = outgoingPaymentService.list().build();
        BigDecimal total = allTimeOutgoingPayments.stream()
                .filter(payment -> (entityType == EntityType.MERCHANT && payment.getMerchantId().equals(entityId))
                        || (entityType == EntityType.TRADER_TEAM && payment.getTraderTeamId().equals(entityId)))
                .filter(payment -> payment.getCurrentState().equals(PaymentState.VERIFIED))
                .map(OutgoingPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    private BigDecimal calculateAllTimeIncomingTotal(String entityId, EntityType entityType) {
        List<IncomingPayment> allTimeIncomingPayments = incomingPaymentService.list().build();
        BigDecimal total = allTimeIncomingPayments.stream()
                .filter(payment -> (entityType == EntityType.MERCHANT && payment.getMerchantId().equals(entityId))
                        || (entityType == EntityType.TRADER_TEAM && payment.getTraderTeamId().equals(entityId)))
                .filter(payment -> payment.getCurrentState().equals(PaymentState.VERIFIED))
                .map(IncomingPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }
}