package com.flacko.payment.webapp.incoming.rest;

import com.flacko.common.exception.IncomingPaymentNotFoundException;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.incoming.IncomingPaymentListBuilder;
import com.flacko.payment.service.incoming.IncomingPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incoming-payments")
public class IncomingPaymentController {

    private static final String MERCHANT_ID = "merchant_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String PAYMENT_METHOD_ID = "payment_method_id";
    private static final String CURRENT_STATE = "current_state";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final IncomingPaymentService incomingPaymentService;
    private final IncomingPaymentRestMapper incomingPaymentRestMapper;

    @GetMapping
    public List<IncomingPaymentResponse> list(@RequestParam(MERCHANT_ID) Optional<String> merchantId,
                                              @RequestParam(TRADER_TEAM_ID) Optional<String> traderTeamId,
                                              @RequestParam(PAYMENT_METHOD_ID) Optional<String> paymentMethodId,
                                              @RequestParam(CURRENT_STATE) Optional<PaymentState> currentState,
                                              @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                              @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        IncomingPaymentListBuilder builder = incomingPaymentService.list();
        merchantId.ifPresent(builder::withMerchantId);
        traderTeamId.ifPresent(builder::withTraderTeamId);
        paymentMethodId.ifPresent(builder::withPaymentMethodId);
        currentState.ifPresent(builder::withCurrentState);

        List<IncomingPaymentResponse> payments = builder.build()
                .stream()
                .map(incomingPaymentRestMapper::mapModelToResponse)
                .collect(Collectors.toList());

        List<IncomingPaymentResponse> initiatedPayments = payments.stream()
                .filter(payment -> payment.currentState() == PaymentState.INITIATED)
                .sorted(Comparator.comparing(IncomingPaymentResponse::createdDate).reversed())
                .collect(Collectors.toList());

        List<IncomingPaymentResponse> otherPayments = payments.stream()
                .filter(payment -> payment.currentState() != PaymentState.INITIATED)
                .sorted(Comparator.comparing(IncomingPaymentResponse::createdDate).reversed())
                .collect(Collectors.toList());

        List<IncomingPaymentResponse> combinedPayments = Stream.concat(initiatedPayments.stream(),
                otherPayments.stream())
                .collect(Collectors.toList());

        return combinedPayments.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping("/{incomingPaymentId}")
    public IncomingPaymentResponse get(@PathVariable String incomingPaymentId) throws IncomingPaymentNotFoundException {
        return incomingPaymentRestMapper.mapModelToResponse(incomingPaymentService.get(incomingPaymentId));
    }

//    @PostMapping("/initiate/incoming")
//    public IncomingPaymentInitiateResponse initiateIncoming(@RequestBody IncomingPaymentInitiateRequest
//    paymentInitiateRequest) {
//        return incomingPaymentRestMapper.mapToInitiateResponse(
//                paymentService.create(incomingPaymentRestMapper.mapToModel(paymentInitiateRequest)));
//    }

}
