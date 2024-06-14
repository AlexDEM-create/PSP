package com.flacko.appeal.impl;

import com.flacko.appeal.service.Appeal;
import com.flacko.appeal.service.AppealBuilder;
import com.flacko.appeal.service.AppealSource;
import com.flacko.appeal.service.AppealState;
import com.flacko.appeal.service.exception.AppealIllegalPaymentCurrentStateException;
import com.flacko.appeal.service.exception.AppealIllegalStateTransitionException;
import com.flacko.appeal.service.exception.AppealMissingRequiredAttributeException;
import com.flacko.common.exception.IncomingPaymentNotFoundException;
import com.flacko.common.exception.MerchantInsufficientOutgoingBalanceException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.NoEligibleTraderTeamsException;
import com.flacko.common.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.common.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.common.exception.OutgoingPaymentMissingRequiredAttributeException;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.common.operation.CrudOperation;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.incoming.IncomingPayment;
import com.flacko.payment.service.incoming.IncomingPaymentService;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class AppealBuilderImpl implements InitializableAppealBuilder {

    private static final Set<PaymentState> ALLOWED_PAYMENT_STATES = Set.of(PaymentState.VERIFIED,
            PaymentState.FAILED_TO_VERIFY, PaymentState.VERIFICATION_EXPIRED);

    private final AppealRepository appealRepository;
    private final IncomingPaymentService incomingPaymentService;
    private final OutgoingPaymentService outgoingPaymentService;

    private AppealPojo.AppealPojoBuilder pojoBuilder;
    private CrudOperation crudOperation;
    private String id;
    private AppealState currentState;

    @Override
    public AppealBuilder initializeNew() {
        crudOperation = CrudOperation.CREATE;
        id = new IdGenerator().generateId();
        currentState = AppealState.INITIATED;
        pojoBuilder = AppealPojo.builder()
                .id(id)
                .currentState(AppealState.INITIATED);
        return this;
    }

    @Override
    public AppealBuilder initializeExisting(Appeal existingAppeal) {
        crudOperation = CrudOperation.UPDATE;
        pojoBuilder = AppealPojo.builder()
                .primaryKey(existingAppeal.getPrimaryKey())
                .id(existingAppeal.getId())
                .paymentId(existingAppeal.getPaymentId())
                .source(existingAppeal.getSource())
                .currentState(existingAppeal.getCurrentState())
                .createdDate(existingAppeal.getCreatedDate())
                .updatedDate(Instant.now());
        id = existingAppeal.getId();
        currentState = existingAppeal.getCurrentState();
        return this;
    }

    @Override
    public AppealBuilder withPaymentId(String paymentId) {
        pojoBuilder.paymentId(paymentId);
        return this;
    }

    @Override
    public AppealBuilder withSource(AppealSource source) {
        pojoBuilder.source(source);
        return this;
    }

    @Override
    public AppealBuilder withState(AppealState newState) throws AppealIllegalStateTransitionException {
        if (!currentState.canChangeTo(newState)) {
            throw new AppealIllegalStateTransitionException(id, currentState, newState);
        }
        pojoBuilder.currentState(newState);
        return this;
    }

    @Transactional
    @Override
    public Appeal build() throws AppealMissingRequiredAttributeException, IncomingPaymentNotFoundException,
            AppealIllegalPaymentCurrentStateException, OutgoingPaymentNotFoundException,
            OutgoingPaymentIllegalStateTransitionException, TraderTeamNotFoundException,
            OutgoingPaymentMissingRequiredAttributeException, PaymentMethodNotFoundException,
            OutgoingPaymentInvalidAmountException, MerchantNotFoundException, NoEligibleTraderTeamsException,
            MerchantInsufficientOutgoingBalanceException {
        AppealPojo appeal = pojoBuilder.build();
        validate(appeal);
        handlePaymentStateChanges(appeal);
        appealRepository.save(appeal);
        return appeal;
    }

    private void validate(AppealPojo pojo) throws AppealMissingRequiredAttributeException,
            IncomingPaymentNotFoundException, AppealIllegalPaymentCurrentStateException,
            OutgoingPaymentNotFoundException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new AppealMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getPaymentId() == null || pojo.getPaymentId().isBlank()) {
            throw new AppealMissingRequiredAttributeException("paymentId", Optional.of(pojo.getId()));
        }
        if (pojo.getSource() == null) {
            throw new AppealMissingRequiredAttributeException("source", Optional.of(pojo.getId()));
        }
        try {
            OutgoingPayment outgoingPayment = outgoingPaymentService.get(pojo.getPaymentId());
            if (crudOperation == CrudOperation.CREATE
                    && !ALLOWED_PAYMENT_STATES.contains(outgoingPayment.getCurrentState())) {
                throw new AppealIllegalPaymentCurrentStateException(pojo.getId(), pojo.getPaymentId(),
                        outgoingPayment.getCurrentState());
            }
        } catch (OutgoingPaymentNotFoundException e) {
            IncomingPayment incomingPayment = incomingPaymentService.get(pojo.getPaymentId());
            if (crudOperation == CrudOperation.CREATE
                    && !ALLOWED_PAYMENT_STATES.contains(incomingPayment.getCurrentState())) {
                throw new AppealIllegalPaymentCurrentStateException(pojo.getId(), pojo.getPaymentId(),
                        incomingPayment.getCurrentState());
            }
        }
        if (pojo.getSource() == AppealSource.TRADER_TEAM) {
            OutgoingPayment outgoingPayment = outgoingPaymentService.get(pojo.getPaymentId());
            if (crudOperation == CrudOperation.CREATE
                    && !ALLOWED_PAYMENT_STATES.contains(outgoingPayment.getCurrentState())) {
                throw new AppealIllegalPaymentCurrentStateException(pojo.getId(), pojo.getPaymentId(),
                        outgoingPayment.getCurrentState());
            }
        }
        if (pojo.getCurrentState() == null) {
            throw new AppealMissingRequiredAttributeException("currentState", Optional.of(pojo.getId()));
        }
    }

    private void handlePaymentStateChanges(AppealPojo appeal) throws OutgoingPaymentNotFoundException,
            NoEligibleTraderTeamsException, OutgoingPaymentIllegalStateTransitionException, TraderTeamNotFoundException,
            OutgoingPaymentMissingRequiredAttributeException, PaymentMethodNotFoundException,
            OutgoingPaymentInvalidAmountException, MerchantNotFoundException,
            MerchantInsufficientOutgoingBalanceException {
        if (appeal.getSource() == AppealSource.TRADER_TEAM) {
            OutgoingPayment outgoingPayment = outgoingPaymentService.get(appeal.getPaymentId());
            switch (appeal.getCurrentState()) {
                case REJECTED -> assignNewTraderTeam(outgoingPayment);
                case RESOLVED -> updatePaymentState(outgoingPayment, PaymentState.VERIFIED);
                case INITIATED -> updatePaymentState(outgoingPayment, PaymentState.DISPUTED);
            }
        }
    }

    private void assignNewTraderTeam(OutgoingPayment outgoingPayment) throws NoEligibleTraderTeamsException,
            OutgoingPaymentNotFoundException, OutgoingPaymentIllegalStateTransitionException,
            TraderTeamNotFoundException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, MerchantNotFoundException,
            MerchantInsufficientOutgoingBalanceException {
        outgoingPaymentService.update(outgoingPayment.getId())
                .withRandomTraderTeamId(Optional.of(outgoingPayment.getTraderTeamId()))
                .withState(PaymentState.INITIATED)
                .build();
    }

    private void updatePaymentState(OutgoingPayment outgoingPayment, PaymentState newState)
            throws OutgoingPaymentNotFoundException, OutgoingPaymentIllegalStateTransitionException,
            TraderTeamNotFoundException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, MerchantNotFoundException,
            MerchantInsufficientOutgoingBalanceException {
        outgoingPaymentService.update(outgoingPayment.getId())
                .withState(newState)
                .build();
    }


}
