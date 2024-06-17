package com.flacko.payment.webapp.rest;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.PaymentListBuilder;
import com.flacko.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private static final String MERCHANT_ID = "merchant_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String PAYMENT_METHOD_ID = "payment_method_id";
    private static final String CURRENCY = "currency";
    private static final String BANK = "bank";
    private static final String CURRENT_STATE = "current_state";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final PaymentService paymentService;
    private final PaymentRestMapper paymentRestMapper;

    @GetMapping
    public List<PaymentResponse> list(@RequestParam(MERCHANT_ID) Optional<String> merchantId,
                                      @RequestParam(TRADER_TEAM_ID) Optional<String> traderTeamId,
                                      @RequestParam(PAYMENT_METHOD_ID) Optional<String> paymentMethodId,
                                      @RequestParam(CURRENCY) Optional<Currency> currency,
                                      @RequestParam(BANK) Optional<Bank> bank,
                                      @RequestParam(CURRENT_STATE) Optional<PaymentState> currentState,
                                      @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                      @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        PaymentListBuilder builder = paymentService.list();
        merchantId.ifPresent(builder::withMerchantId);
        traderTeamId.ifPresent(builder::withTraderTeamId);
        paymentMethodId.ifPresent(builder::withPaymentMethodId);
        currency.ifPresent(builder::withCurrency);
        bank.ifPresent(builder::withBank);
        currentState.ifPresent(builder::withCurrentState);
        return builder.build()
                .stream()
                .map(paymentRestMapper::mapModelToResponse)
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

}
