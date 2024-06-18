package com.flacko.payment.service.incoming;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.incoming.exception.IncomingPaymentIllegalStateTransitionException;
import com.flacko.payment.service.incoming.exception.IncomingPaymentInvalidAmountException;
import com.flacko.payment.service.incoming.exception.IncomingPaymentMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface IncomingPaymentBuilder {

    IncomingPaymentBuilder withMerchantId(String merchantId);

    IncomingPaymentBuilder withTraderTeamId(String traderTeamId);

    IncomingPaymentBuilder withPaymentMethodId(String paymentMethodId);

    IncomingPaymentBuilder withAmount(BigDecimal amount);

    IncomingPaymentBuilder withCurrency(Currency currency);

    IncomingPaymentBuilder withBank(Bank bank);

    IncomingPaymentBuilder withState(PaymentState newState) throws IncomingPaymentIllegalStateTransitionException;

    IncomingPayment build() throws IncomingPaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, PaymentMethodNotFoundException, IncomingPaymentInvalidAmountException;

}
