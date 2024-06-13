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
import com.flacko.common.exception.UnauthorizedAccessException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentBuilder;
import com.flacko.payment.service.outgoing.OutgoingPaymentListBuilder;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OutgoingPaymentServiceImpl implements OutgoingPaymentService {

    private final OutgoingPaymentRepository outgoingPaymentRepository;
    private final ServiceLocator serviceLocator;
    private final TraderTeamService traderTeamService;
    private final UserService userService;

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
            OutgoingPaymentNotFoundException, UnauthorizedAccessException, NoEligibleTraderTeamsException,
            OutgoingPaymentIllegalStateTransitionException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, MerchantNotFoundException,
            UserNotFoundException, MerchantInsufficientOutgoingBalanceException {
        String userId = userService.getByLogin(login)
                .getId();
        TraderTeam currentTraderTeam = traderTeamService.getByUserId(userId);
        OutgoingPayment outgoingPayment = get(id);
        if (!outgoingPayment.getTraderTeamId().equals(currentTraderTeam.getId())) {
            throw new UnauthorizedAccessException(String.format("User %s is not allowed to refuse outgoing payment %s",
                    login, id));
        }
        return update(id)
                .withRandomTraderTeamId(Optional.of(outgoingPayment.getTraderTeamId()))
                .withState(PaymentState.INITIATED)
                .build();
    }

}
