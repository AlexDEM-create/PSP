package com.flacko.payment.verification.webapp.rest;

import com.flacko.common.exception.IncomingPaymentNotFoundException;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.exception.ReceiptPaymentVerificationNotFoundException;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationListBuilder;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationService;
import com.flacko.payment.verification.receipt.service.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-verifications/receipts")
public class ReceiptPaymentVerificationController {

    private static final String FILE = "file";
    private static final String OUTGOING_PAYMENT_ID = "outgoing_payment_id";

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
                receiptPaymentVerificationService.get(outgoingPaymentId));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ReceiptPaymentVerificationResponse verify(@RequestParam(FILE) MultipartFile file,
                                                     @RequestParam(OUTGOING_PAYMENT_ID) String outgoingPaymentId)
            throws ReceiptPaymentVerificationRequestValidationException, ReceiptPaymentVerificationFailedException,
            ReceiptPaymentVerificationCurrencyNotSupportedException, IncomingPaymentNotFoundException,
            ReceiptPaymentVerificationInvalidCardLastFourDigitsException,
            ReceiptPaymentVerificationMissingRequiredAttributeException,
            ReceiptPaymentVerificationInvalidAmountException, ReceiptPaymentVerificationUnexpectedAmountException,
            OutgoingPaymentNotFoundException {
        return receiptPaymentVerificationRestMapper.mapModelToResponse(
                receiptPaymentVerificationService.verify(file, outgoingPaymentId));
    }

}
