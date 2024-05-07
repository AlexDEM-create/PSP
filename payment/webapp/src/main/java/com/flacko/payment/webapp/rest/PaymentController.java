package com.flacko.payment.webapp.rest;

import com.flacko.common.exception.PaymentNotFoundException;
import com.flacko.payment.service.PaymentListBuilder;
import com.flacko.payment.service.PaymentService;
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
    public List<PaymentResponse> list(PaymentFilterRequest paymentFilterRequest) {
        PaymentListBuilder builder = paymentService.list();
        paymentFilterRequest.merchantId().ifPresent(builder::withMerchantId);
        paymentFilterRequest.traderTeamId().ifPresent(builder::withTraderTeamId);
        paymentFilterRequest.cardId().ifPresent(builder::withCardId);
        paymentFilterRequest.direction().ifPresent(builder::withDirection);
        paymentFilterRequest.currentState().ifPresent(builder::withCurrentState);
        return builder.build()
                .stream()
                .map(paymentRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse get(@PathVariable String paymentId) throws PaymentNotFoundException {
        return paymentRestMapper.mapModelToResponse(paymentService.get(paymentId));
    }

    @PostMapping("/incoming")
    public PaymentCreateResponse createIncoming(@RequestBody PaymentCreateRequest paymentCreateRequest) {
        return
    }

    @PostMapping("/outgoing")
    public PaymentCreateResponse createOutgoing(@RequestBody PaymentCreateRequest paymentCreateRequest) {
        return
    }

}
