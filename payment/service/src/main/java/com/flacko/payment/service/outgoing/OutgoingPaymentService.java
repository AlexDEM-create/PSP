package com.flacko.payment.service.outgoing;

import com.flacko.common.exception.*;

public interface OutgoingPaymentService {

    OutgoingPaymentBuilder create(String login) throws UserNotFoundException, MerchantNotFoundException;

    OutgoingPaymentBuilder update(String id) throws OutgoingPaymentNotFoundException;

    OutgoingPaymentListBuilder list();

    OutgoingPayment get(String id) throws OutgoingPaymentNotFoundException;

    OutgoingPayment reassignRandomTraderTeam(String id, String login) throws TraderTeamNotFoundException, OutgoingPaymentNotFoundException, UnauthorizedAccessException, NoEligibleTraderTeamsException, OutgoingPaymentIllegalStateTransitionException, OutgoingPaymentMissingRequiredAttributeException, PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, MerchantNotFoundException, UserNotFoundException, MerchantInsufficientOutgoingBalanceException;

}
