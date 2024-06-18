package com.flacko.stats.webapp;

import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.service.incoming.IncomingPayment;
import com.flacko.payment.service.incoming.IncomingPaymentListBuilder;
import com.flacko.payment.service.incoming.IncomingPaymentService;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentListBuilder;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.stats.service.EntityType;
import com.flacko.stats.service.StatsService;
import com.flacko.stats.service.exception.StatsMissingRequiredAttributeException;
import com.flacko.stats.service.exception.StatsNotFoundException;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatsScheduler {

    private final StatsService statsService;
    private final MerchantService merchantService;
    private final TraderTeamService traderTeamService;
    private final OutgoingPaymentService outgoingPaymentService;
    private final IncomingPaymentService incomingPaymentService;

    @Scheduled(fixedRate = 60000)
    public void collectStats() {
        Instant now = Instant.now();
        LocalDate today = LocalDate.now();
        Instant startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant();

        collectStatsForMerchants(startOfDay, now);
        collectStatsForTraderTeams(startOfDay, now);
    }

    private void collectStatsForMerchants(Instant startOfDay, Instant now) {
        List<Merchant> merchants = fetchAllMerchants();

        for (Merchant merchant : merchants) {
            BigDecimal todayIncomingTotal = calculateTotalIncoming(Optional.of(startOfDay), now,
                    Optional.of(merchant.getId()), Optional.empty());
            BigDecimal allTimeIncomingTotal = calculateTotalIncoming(Optional.empty(), now,
                    Optional.of(merchant.getId()), Optional.empty());
            BigDecimal todayOutgoingTotal = calculateTotalOutgoing(Optional.of(startOfDay), now,
                    Optional.of(merchant.getId()), Optional.empty());
            BigDecimal allTimeOutgoingTotal = calculateTotalOutgoing(Optional.empty(), now,
                    Optional.of(merchant.getId()), Optional.empty());

            updateStats(merchant.getId(), EntityType.MERCHANT, todayIncomingTotal, allTimeIncomingTotal,
                    todayOutgoingTotal, allTimeOutgoingTotal);
        }
    }

    private void collectStatsForTraderTeams(Instant startOfDay, Instant now) {
        List<TraderTeam> traderTeams = fetchAllTraderTeams();

        for (TraderTeam traderTeam : traderTeams) {
            BigDecimal todayIncomingTotal = calculateTotalIncoming(Optional.of(startOfDay), now,
                    Optional.empty(), Optional.of(traderTeam.getId()));
            BigDecimal allTimeIncomingTotal = calculateTotalIncoming(Optional.empty(), now,
                    Optional.empty(), Optional.of(traderTeam.getId()));
            BigDecimal todayOutgoingTotal = calculateTotalOutgoing(Optional.of(startOfDay), now,
                    Optional.empty(), Optional.of(traderTeam.getId()));
            BigDecimal allTimeOutgoingTotal = calculateTotalOutgoing(Optional.empty(), now,
                    Optional.empty(), Optional.of(traderTeam.getId()));

            updateStats(traderTeam.getId(), EntityType.TRADER_TEAM, todayIncomingTotal, allTimeIncomingTotal,
                    todayOutgoingTotal, allTimeOutgoingTotal);
        }
    }

    private BigDecimal calculateTotalIncoming(Optional<Instant> startDate, Instant endDate, Optional<String> merchantId,
                                              Optional<String> traderTeamId) {
        IncomingPaymentListBuilder builder = incomingPaymentService.list()
                .withCurrentState(PaymentState.VERIFIED);

        merchantId.ifPresent(builder::withMerchantId);
        traderTeamId.ifPresent(builder::withTraderTeamId);
        startDate.ifPresent(builder::withStartDate);
        builder.withEndDate(endDate);

        List<IncomingPayment> incomingPayments = builder.build();
        return incomingPayments.stream()
                .map(IncomingPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalOutgoing(Optional<Instant> startDate, Instant endDate, Optional<String> merchantId,
                                              Optional<String> traderTeamId) {
        OutgoingPaymentListBuilder builder = outgoingPaymentService.list()
                .withCurrentState(PaymentState.VERIFIED);

        merchantId.ifPresent(builder::withMerchantId);
        traderTeamId.ifPresent(builder::withTraderTeamId);
        startDate.ifPresent(builder::withStartDate);
        builder.withEndDate(endDate);

        List<OutgoingPayment> outgoingPayments = builder.build();
        return outgoingPayments.stream()
                .map(OutgoingPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updateStats(String entityId, EntityType entityType, BigDecimal todayIncomingTotal,
                             BigDecimal allTimeIncomingTotal, BigDecimal todayOutgoingTotal,
                             BigDecimal allTimeOutgoingTotal) {
        try {
            try {
                statsService.update(entityId, entityType)
                        .withTodayIncomingTotal(todayIncomingTotal)
                        .withAllTimeIncomingTotal(allTimeIncomingTotal)
                        .withTodayOutgoingTotal(todayOutgoingTotal)
                        .withAllTimeOutgoingTotal(allTimeOutgoingTotal)
                        .build();
            } catch (StatsNotFoundException e) {
                statsService.create()
                        .withEntityId(entityId)
                        .withEntityType(entityType)
                        .withTodayIncomingTotal(todayIncomingTotal)
                        .withAllTimeIncomingTotal(allTimeIncomingTotal)
                        .withTodayOutgoingTotal(todayOutgoingTotal)
                        .withAllTimeOutgoingTotal(allTimeOutgoingTotal)
                        .build();
            }
        } catch (StatsMissingRequiredAttributeException | MerchantNotFoundException | TraderTeamNotFoundException e) {
            log.error(String.format("Could not update stats for %s %s with today incoming total %s, " +
                            "all time incoming total %s, today outgoing total %s, all time outgoing total %s",
                    entityType, entityId, todayIncomingTotal, allTimeIncomingTotal, todayOutgoingTotal,
                    allTimeOutgoingTotal), e);
        }
    }

    private List<Merchant> fetchAllMerchants() {
        return merchantService.list()
                .withArchived(false)
                .build();
    }

    private List<TraderTeam> fetchAllTraderTeams() {
        return traderTeamService.list()
                .withArchived(false)
                .build();
    }

}
