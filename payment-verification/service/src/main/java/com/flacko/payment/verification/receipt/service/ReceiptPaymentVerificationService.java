package com.flacko.payment.verification.receipt.service;

import com.flacko.common.exception.BalanceInvalidCurrentBalanceException;
import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.BalanceNotFoundException;
import com.flacko.common.exception.IncomingPaymentNotFoundException;
import com.flacko.common.exception.MerchantInsufficientOutgoingBalanceException;
import com.flacko.common.exception.MerchantInvalidFeeRateException;
import com.flacko.common.exception.MerchantMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.NoEligibleTraderTeamsException;
import com.flacko.common.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.common.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.common.exception.OutgoingPaymentMissingRequiredAttributeException;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.ReceiptPaymentVerificationNotFoundException;
import com.flacko.common.exception.TraderTeamIllegalLeaderException;
import com.flacko.common.exception.TraderTeamInvalidFeeRateException;
import com.flacko.common.exception.TraderTeamMissingRequiredAttributeException;
import com.flacko.common.exception.TraderTeamNotAllowedOnlineException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UnauthorizedAccessException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationCurrencyNotSupportedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationFailedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationInvalidAmountException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationMissingRequiredAttributeException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationRequestValidationException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationUnexpectedAmountException;
import org.springframework.web.multipart.MultipartFile;

public interface ReceiptPaymentVerificationService {

    ReceiptPaymentVerificationListBuilder list();

    ReceiptPaymentVerification getByOutgoingPaymentId(String outgoingPaymentId)
            throws ReceiptPaymentVerificationNotFoundException;

    ReceiptPaymentVerification verify(MultipartFile file, String outgoingPaymentId, String paymentMethodId)
            throws ReceiptPaymentVerificationRequestValidationException, ReceiptPaymentVerificationFailedException,
            ReceiptPaymentVerificationCurrencyNotSupportedException, IncomingPaymentNotFoundException,
            ReceiptPaymentVerificationMissingRequiredAttributeException,
            ReceiptPaymentVerificationInvalidAmountException, ReceiptPaymentVerificationUnexpectedAmountException,
            OutgoingPaymentNotFoundException, PaymentMethodNotFoundException, TraderTeamNotFoundException,
            BalanceNotFoundException, MerchantNotFoundException, BalanceMissingRequiredAttributeException,
            OutgoingPaymentIllegalStateTransitionException, OutgoingPaymentMissingRequiredAttributeException,
            OutgoingPaymentInvalidAmountException, UserNotFoundException, BalanceInvalidCurrentBalanceException,
            MerchantInvalidFeeRateException, MerchantMissingRequiredAttributeException,
            MerchantInsufficientOutgoingBalanceException, TraderTeamMissingRequiredAttributeException,
            TraderTeamNotAllowedOnlineException, UnauthorizedAccessException, TraderTeamInvalidFeeRateException,
            NoEligibleTraderTeamsException, TraderTeamIllegalLeaderException;

}
