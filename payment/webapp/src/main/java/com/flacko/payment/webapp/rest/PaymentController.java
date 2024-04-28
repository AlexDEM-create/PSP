package com.flacko.payment.webapp.rest;

import com.flacko.common.exception.PaymentNotFoundException;
import com.flacko.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @PostMapping("/initiate/incoming")
//    public PaymentInitiateResponse initiateIncoming(@RequestBody PaymentInitiateRequest paymentInitiateRequest) {
//        return paymentRestMapper.mapToInitiateResponse(
//                paymentService.create(paymentRestMapper.mapToModel(paymentInitiateRequest)));
//    }
//
//    @PostMapping("/initiate/outgoing")
//    public PaymentInitiateResponse initiateOutgoing(@RequestBody PaymentInitiateRequest paymentInitiateRequest) {
//        return paymentRestMapper.mapToInitiateResponse(
//                paymentService.create(paymentRestMapper.mapToModel(paymentInitiateRequest)));
//    }

}
