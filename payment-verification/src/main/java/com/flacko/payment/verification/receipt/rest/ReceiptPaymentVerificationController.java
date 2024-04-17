package com.flacko.payment.verification.receipt.rest;

import com.flacko.payment.verification.receipt.ReceiptPaymentVerificationService;
import com.flacko.payment.verification.receipt.exception.ReceiptPaymentVerificationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-verification/receipt")
public class ReceiptPaymentVerificationController {

    private static final String FILE = "file";
    private static final String TRADER_ID = "trader_id";
    private static final String PAYMENT_ID = "payment_id";
    private static final String MERCHANT_ID = "merchant_id";

    private final ReceiptPaymentVerificationService receiptPaymentVerificationService;
    private final ReceiptPaymentVerificationRestMapper receiptPaymentVerificationRestMapper;

    @GetMapping
    public List<ReceiptPaymentVerificationResponse> list() {
        return receiptPaymentVerificationService.list()
                .stream()
                .map(receiptPaymentVerificationRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{paymentId}")
    public ReceiptPaymentVerificationResponse get(@PathVariable String paymentId) throws ReceiptPaymentVerificationNotFoundException {
        return receiptPaymentVerificationRestMapper.mapModelToResponse(receiptPaymentVerificationService.get(paymentId));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ReceiptPaymentVerificationResponse verify(@RequestParam(FILE) MultipartFile file,
                                                     @RequestParam(PAYMENT_ID) String paymentId) throws Exception {
        ReceiptPaymentVerificationRequest receiptPaymentVerificationRequest = new ReceiptPaymentVerificationRequest(file, paymentId);
        return receiptPaymentVerificationRestMapper.mapModelToResponse(
                receiptPaymentVerificationService.verify(receiptPaymentVerificationRequest));
    }

}
