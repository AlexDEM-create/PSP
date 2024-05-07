package com.flacko.payment.verification.webapp.rest;

import com.flacko.common.exception.ReceiptPaymentVerificationNotFoundException;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationListBuilder;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationService;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationFailedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationRequestValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-verifications/receipts")
public class ReceiptPaymentVerificationController {

    private static final String FILE = "file";
    private static final String PAYMENT_ID = "payment_id";

    private final ReceiptPaymentVerificationService receiptPaymentVerificationService;
    private final ReceiptPaymentVerificationRestMapper receiptPaymentVerificationRestMapper;

    @GetMapping
    public List<ReceiptPaymentVerificationResponse> list(
            ReceiptPaymentVerificationFilterRequest receiptPaymentVerificationFilterRequest) {
        ReceiptPaymentVerificationListBuilder builder = receiptPaymentVerificationService.list();
        receiptPaymentVerificationFilterRequest.paymentId().ifPresent(builder::withPaymentId);
        return builder.build()
                .stream()
                .map(receiptPaymentVerificationRestMapper::mapModelToResponse)
                .skip(receiptPaymentVerificationFilterRequest.offset())
                .limit(receiptPaymentVerificationFilterRequest.limit())
                .collect(Collectors.toList());
    }

    @GetMapping("/{paymentId}")
    public ReceiptPaymentVerificationResponse get(@PathVariable String paymentId)
            throws ReceiptPaymentVerificationNotFoundException {
        return receiptPaymentVerificationRestMapper.mapModelToResponse(receiptPaymentVerificationService.get(paymentId));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ReceiptPaymentVerificationResponse verify(@RequestParam(FILE) MultipartFile file,
                                                     @RequestParam(PAYMENT_ID) String paymentId)
            throws ReceiptPaymentVerificationRequestValidationException, ReceiptPaymentVerificationFailedException {
        return receiptPaymentVerificationRestMapper.mapModelToResponse(
                receiptPaymentVerificationService.verify(file, paymentId));
    }

}
