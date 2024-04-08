package com.flacko.payment.rest;

import com.flacko.payment.PaymentService;
import com.flacko.payment.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRestMapper paymentRestMapper;

    @GetMapping
    public List<PaymentResponse> list() {
        return paymentService.list()
                .stream()
                .map(paymentRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse get(@PathVariable String paymentId) throws PaymentNotFoundException {
        return paymentRestMapper.mapModelToResponse(paymentService.get(paymentId));
    }

    @PostMapping("/initiate/incoming")
    public PaymentInitiateResponse initiateIncoming(@RequestBody PaymentInitiateRequest paymentInitiateRequest) {
        return paymentRestMapper.mapToInitiateResponse(
                paymentService.create(paymentRestMapper.mapToModel(paymentInitiateRequest)));
    }

    @PostMapping("/initiate/outgoing")
    public PaymentInitiateResponse initiateOutgoing(@RequestBody PaymentInitiateRequest paymentInitiateRequest) {
        return paymentRestMapper.mapToInitiateResponse(
                paymentService.create(paymentRestMapper.mapToModel(paymentInitiateRequest)));
    }

}
