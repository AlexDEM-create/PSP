package com.flacko.incoming.payment.webapp.rest;

import com.flacko.common.exception.PaymentNotFoundException;
import com.flacko.incoming.payment.service.IncomingPaymentService;
import com.flacko.payment.service.PaymentListBuilder;
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
public class IncomingPaymentController {

    private final IncomingPaymentService incomingPaymentService;
    private final IncomingPaymentRestMapper incomingPaymentRestMapper;

    @GetMapping
    public List<IncomingPaymentResponse> list(IncomingPaymentFilterRequest incomingPaymentFilterRequest) {
        PaymentListBuilder builder = incomingPaymentService.list();
        incomingPaymentFilterRequest.merchantId().ifPresent(builder::withMerchantId);
        incomingPaymentFilterRequest.traderTeamId().ifPresent(builder::withTraderTeamId);
        incomingPaymentFilterRequest.cardId().ifPresent(builder::withCardId);
        incomingPaymentFilterRequest.direction().ifPresent(builder::withDirection);
        incomingPaymentFilterRequest.currentState().ifPresent(builder::withCurrentState);
        return builder.build()
                .stream()
                .map(incomingPaymentRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{paymentId}")
    public IncomingPaymentResponse get(@PathVariable String paymentId) throws PaymentNotFoundException {
        return incomingPaymentRestMapper.mapModelToResponse(incomingPaymentService.get(paymentId));
    }

//    @PostMapping("/initiate/incoming")
//    public IncomingPaymentInitiateResponse initiateIncoming(@RequestBody IncomingPaymentInitiateRequest paymentInitiateRequest) {
//        return incomingPaymentRestMapper.mapToInitiateResponse(
//                paymentService.create(incomingPaymentRestMapper.mapToModel(paymentInitiateRequest)));
//    }
//
//    @PostMapping("/initiate/outgoing")
//    public IncomingPaymentInitiateResponse initiateOutgoing(@RequestBody IncomingPaymentInitiateRequest paymentInitiateRequest) {
//        return incomingPaymentRestMapper.mapToInitiateResponse(
//                paymentService.create(incomingPaymentRestMapper.mapToModel(paymentInitiateRequest)));
//    }

}
