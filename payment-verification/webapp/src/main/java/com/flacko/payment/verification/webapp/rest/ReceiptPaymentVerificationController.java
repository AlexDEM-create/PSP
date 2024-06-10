package com.flacko.payment.verification.webapp.rest;

import com.flacko.common.exception.BalanceInvalidCurrentBalanceException;
import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.BalanceNotFoundException;
import com.flacko.common.exception.IncomingPaymentNotFoundException;
import com.flacko.common.exception.MerchantInsufficientOutgoingBalanceException;
import com.flacko.common.exception.MerchantInvalidFeeRateException;
import com.flacko.common.exception.MerchantMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.common.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.common.exception.OutgoingPaymentMissingRequiredAttributeException;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.ReceiptPaymentVerificationNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationListBuilder;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationService;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationCurrencyNotSupportedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationFailedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationInvalidAmountException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationMissingRequiredAttributeException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationRequestValidationException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationUnexpectedAmountException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/payment-verifications/receipts")
public class ReceiptPaymentVerificationController {

    private static final String FILE = "file";
    private static final String OUTGOING_PAYMENT_ID = "outgoing_payment_id";
    private static final String PAYMENT_METHOD_ID = "payment_method_id";

    private final ReceiptPaymentVerificationService receiptPaymentVerificationService;
    private final ReceiptPaymentVerificationRestMapper receiptPaymentVerificationRestMapper;

    @GetMapping
    public List<ReceiptPaymentVerificationResponse> list(
            @RequestParam(OUTGOING_PAYMENT_ID) Optional<String> outgoingPaymentId,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        ReceiptPaymentVerificationListBuilder builder = receiptPaymentVerificationService.list();
        outgoingPaymentId.ifPresent(builder::withOutgoingPaymentId);
        return builder.build()
                .stream()
                .map(receiptPaymentVerificationRestMapper::mapModelToResponse)
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping("/{outgoingPaymentId}")
    public ReceiptPaymentVerificationResponse get(@PathVariable String outgoingPaymentId)
            throws ReceiptPaymentVerificationNotFoundException {
        return receiptPaymentVerificationRestMapper.mapModelToResponse(
                receiptPaymentVerificationService.getByOutgoingPaymentId(outgoingPaymentId));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ReceiptPaymentVerificationResponse verify(@RequestParam(FILE) MultipartFile file,
                                                     @RequestParam(OUTGOING_PAYMENT_ID) String outgoingPaymentId,
                                                     @RequestParam(PAYMENT_METHOD_ID) String paymentMethodId)
            throws ReceiptPaymentVerificationRequestValidationException, ReceiptPaymentVerificationFailedException,
            ReceiptPaymentVerificationCurrencyNotSupportedException, IncomingPaymentNotFoundException,
            ReceiptPaymentVerificationMissingRequiredAttributeException,
            ReceiptPaymentVerificationInvalidAmountException, ReceiptPaymentVerificationUnexpectedAmountException,
            OutgoingPaymentNotFoundException, TraderTeamNotFoundException, BalanceNotFoundException,
            PaymentMethodNotFoundException, MerchantNotFoundException, BalanceMissingRequiredAttributeException,
            OutgoingPaymentIllegalStateTransitionException, OutgoingPaymentMissingRequiredAttributeException,
            OutgoingPaymentInvalidAmountException, UserNotFoundException, BalanceInvalidCurrentBalanceException,
            MerchantInvalidFeeRateException, MerchantMissingRequiredAttributeException,
            MerchantInsufficientOutgoingBalanceException {
        return receiptPaymentVerificationRestMapper.mapModelToResponse(
                receiptPaymentVerificationService.verify(file, outgoingPaymentId, paymentMethodId));
    }

}
