package com.flacko.payment.service.outgoing;

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

public interface OutgoingPaymentService {

    OutgoingPaymentBuilder create(String login) throws UserNotFoundException, MerchantNotFoundException;

    OutgoingPaymentBuilder update(String id) throws OutgoingPaymentNotFoundException;

    OutgoingPaymentListBuilder list();

    OutgoingPayment get(String id) throws OutgoingPaymentNotFoundException;

    OutgoingPayment reassignRandomTraderTeam(String id, String login) throws TraderTeamNotFoundException,
            OutgoingPaymentNotFoundException, NoEligibleTraderTeamsException,
            OutgoingPaymentIllegalStateTransitionException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, MerchantNotFoundException,
            MerchantInsufficientOutgoingBalanceException, UserNotFoundException;

}
