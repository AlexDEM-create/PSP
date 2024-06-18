package com.flacko.payment.impl.outgoing;

import com.flacko.common.exception.MerchantInsufficientOutgoingBalanceException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.NoEligibleTraderTeamsException;
import com.flacko.common.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.common.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.common.exception.OutgoingPaymentMissingRequiredAttributeException;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentBuilder;
import com.flacko.payment.service.outgoing.OutgoingPaymentListBuilder;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OutgoingPaymentServiceImpl implements OutgoingPaymentService {

    private final OutgoingPaymentRepository outgoingPaymentRepository;
    private final ServiceLocator serviceLocator;

    @Transactional
    @Override
    public OutgoingPaymentBuilder create(String login) throws UserNotFoundException, MerchantNotFoundException {
        return serviceLocator.create(InitializableOutgoingPaymentBuilder.class)
                .initializeNew(login);
    }

    @Transactional
    @Override
    public OutgoingPaymentBuilder update(String id) throws OutgoingPaymentNotFoundException {
        OutgoingPayment existingOutgoingPayment = get(id);
        return serviceLocator.create(InitializableOutgoingPaymentBuilder.class)
                .initializeExisting(existingOutgoingPayment);
    }

    @Override
    public OutgoingPaymentListBuilder list() {
        return serviceLocator.create(OutgoingPaymentListBuilder.class);
    }

    @Override
    public OutgoingPayment get(String id) throws OutgoingPaymentNotFoundException {
        return outgoingPaymentRepository.findById(id)
                .orElseThrow(() -> new OutgoingPaymentNotFoundException(id));
    }

    @Transactional
    @Override
    public OutgoingPayment reassignRandomTraderTeam(String id, String login) throws TraderTeamNotFoundException,
            OutgoingPaymentNotFoundException, NoEligibleTraderTeamsException,
            OutgoingPaymentIllegalStateTransitionException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, MerchantNotFoundException,
            MerchantInsufficientOutgoingBalanceException, UserNotFoundException {
        OutgoingPayment outgoingPayment = get(id);
        return update(id)
                .withRandomTraderTeamId(Optional.of(outgoingPayment.getTraderTeamId()))
                .withState(PaymentState.INITIATED)
                .build();
    }

}
