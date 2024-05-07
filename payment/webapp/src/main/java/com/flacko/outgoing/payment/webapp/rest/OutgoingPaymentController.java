package com.flacko.outgoing.payment.webapp.rest;

import com.flacko.common.exception.PaymentNotFoundException;
import com.flacko.outgoing.payment.service.OutgoingPaymentListBuilder;
import com.flacko.outgoing.payment.service.OutgoingPaymentService;
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
public class OutgoingPaymentController {

    private final OutgoingPaymentService outgoingPaymentService;
    private final OutgoingPaymentRestMapper outgoingPaymentRestMapper;

    @GetMapping
    public List<OutgoingPaymentResponse> list(OutgoingPaymentFilterRequest outgoingPaymentFilterRequest) {
        OutgoingPaymentListBuilder builder = outgoingPaymentService.list();
        outgoingPaymentFilterRequest.merchantId().ifPresent(builder::withMerchantId);
        outgoingPaymentFilterRequest.traderTeamId().ifPresent(builder::withTraderTeamId);
        outgoingPaymentFilterRequest.cardId().ifPresent(builder::withCardId);
        outgoingPaymentFilterRequest.direction().ifPresent(builder::withDirection);
        outgoingPaymentFilterRequest.currentState().ifPresent(builder::withCurrentState);
        return builder.build()
                .stream()
                .map(outgoingPaymentRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{paymentId}")
    public OutgoingPaymentResponse get(@PathVariable String paymentId) throws PaymentNotFoundException {
        return outgoingPaymentRestMapper.mapModelToResponse(outgoingPaymentService.get(paymentId));
    }

//    @PostMapping("/initiate/incoming")
//    public IncomingPaymentInitiateResponse initiateIncoming(@RequestBody IncomingPaymentInitiateRequest paymentInitiateRequest) {
//        return paymentRestMapper.mapToInitiateResponse(
//                paymentService.create(paymentRestMapper.mapToModel(paymentInitiateRequest)));
//    }
//
//    @PostMapping("/initiate/outgoing")
//    public IncomingPaymentInitiateResponse initiateOutgoing(@RequestBody IncomingPaymentInitiateRequest paymentInitiateRequest) {
//        return paymentRestMapper.mapToInitiateResponse(
//                paymentService.create(paymentRestMapper.mapToModel(paymentInitiateRequest)));
//    }

}
