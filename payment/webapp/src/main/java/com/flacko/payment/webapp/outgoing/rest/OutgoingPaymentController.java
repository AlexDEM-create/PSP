package com.flacko.payment.webapp.outgoing.rest;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.outgoing.OutgoingPaymentListBuilder;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/outgoing-payments")
public class OutgoingPaymentController {

    private static final String MERCHANT_ID = "merchant_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String PAYMENT_METHOD_ID = "payment_method_id";
    private static final String CURRENCY = "currency";
    private static final String RECIPIENT = "recipient";
    private static final String BANK = "bank";
    private static final String RECIPIENT_PAYMENT_METHOD_TYPE = "recipient_payment_method_type";
    private static final String CURRENT_STATE = "current_state";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final OutgoingPaymentService outgoingPaymentService;
    private final OutgoingPaymentRestMapper outgoingPaymentRestMapper;

    @GetMapping
    public List<OutgoingPaymentResponse> list(@RequestParam(MERCHANT_ID) Optional<String> merchantId,
                                              @RequestParam(TRADER_TEAM_ID) Optional<String> traderTeamId,
                                              @RequestParam(PAYMENT_METHOD_ID) Optional<String> paymentMethodId,
                                              @RequestParam(CURRENCY) Optional<Currency> currency,
                                              @RequestParam(RECIPIENT) Optional<String> recipient,
                                              @RequestParam(BANK) Optional<Bank> bank,
                                              @RequestParam(RECIPIENT_PAYMENT_METHOD_TYPE)
                                                      Optional<RecipientPaymentMethodType> recipientPaymentMethodType,
                                              @RequestParam(CURRENT_STATE) Optional<PaymentState> currentState,
                                              @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                              @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        OutgoingPaymentListBuilder builder = outgoingPaymentService.list();
        merchantId.ifPresent(builder::withMerchantId);
        traderTeamId.ifPresent(builder::withTraderTeamId);
        paymentMethodId.ifPresent(builder::withPaymentMethodId);
        currency.ifPresent(builder::withCurrency);
        recipient.ifPresent(builder::withRecipient);
        bank.ifPresent(builder::withBank);
        recipientPaymentMethodType.ifPresent(builder::withRecipientPaymentMethodType);
        currentState.ifPresent(builder::withCurrentState);
        return builder.build()
                .stream()
                .map(outgoingPaymentRestMapper::mapModelToResponse)
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping("/{outgoingPaymentId}")
    public OutgoingPaymentResponse get(@PathVariable String outgoingPaymentId) throws OutgoingPaymentNotFoundException {
        return outgoingPaymentRestMapper.mapModelToResponse(outgoingPaymentService.get(outgoingPaymentId));
    }

//    @PostMapping("/initiate/outgoing")
//    public IncomingPaymentInitiateResponse initiateOutgoing(@RequestBody IncomingPaymentInitiateRequest paymentInitiateRequest) {
//        return paymentRestMapper.mapToInitiateResponse(
//                paymentService.create(paymentRestMapper.mapToModel(paymentInitiateRequest)));
//    }

}
