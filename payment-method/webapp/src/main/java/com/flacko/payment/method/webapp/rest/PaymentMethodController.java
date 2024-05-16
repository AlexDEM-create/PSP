package com.flacko.payment.method.webapp.rest;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.BankNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.method.service.PaymentMethodBuilder;
import com.flacko.payment.method.service.PaymentMethodListBuilder;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.method.service.exception.PaymentMethodInvalidBankCardNumberException;
import com.flacko.payment.method.service.exception.PaymentMethodMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-methods")
public class PaymentMethodController {

    private static final String CURRENCY = "currency";
    private static final String BANK = "bank";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String TERMINAL_ID = "terminal_id";
    private static final String BUSY = "busy";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final PaymentMethodService paymentMethodService;
    private final PaymentRestMapper paymentRestMapper;

    @GetMapping
    public List<PaymentMethodResponse> list(@RequestParam(CURRENCY) Optional<Currency> currency,
                                            @RequestParam(BANK) Optional<Bank> bank,
                                            @RequestParam(TRADER_TEAM_ID) Optional<String> traderTeamId,
                                            @RequestParam(TERMINAL_ID) Optional<String> terminalId,
                                            @RequestParam(BUSY) Optional<Boolean> busy,
                                            @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                            @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        PaymentMethodListBuilder builder = paymentMethodService.list();
        currency.ifPresent(builder::withCurrency);
        bank.ifPresent(builder::withBank);
        traderTeamId.ifPresent(builder::withTraderTeamId);
        terminalId.ifPresent(builder::withTerminalId);
        busy.ifPresent(builder::withBusy);
        return builder.build()
                .stream()
                .map(paymentRestMapper::mapModelToResponse)
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping("/{paymentMethodId}")
    public PaymentMethodResponse get(@PathVariable String paymentMethodId) throws PaymentMethodNotFoundException {
        return paymentRestMapper.mapModelToResponse(paymentMethodService.get(paymentMethodId));
    }

    @PostMapping
    public PaymentMethodResponse create(@RequestBody PaymentMethodCreateRequest paymentMethodCreateRequest)
            throws PaymentMethodMissingRequiredAttributeException, TraderTeamNotFoundException,
            PaymentMethodInvalidBankCardNumberException, BankNotFoundException, TerminalNotFoundException {
        PaymentMethodBuilder builder = paymentMethodService.create();
        builder.withNumber(paymentMethodCreateRequest.number())
                .withFirstName(paymentMethodCreateRequest.firstName())
                .withLastName(paymentMethodCreateRequest.lastName())
                .withCurrency(paymentMethodCreateRequest.currency())
                .withBank(paymentMethodCreateRequest.bank())
                .withTraderTeamId(paymentMethodCreateRequest.traderTeamId())
                .withTerminalId(paymentMethodCreateRequest.terminalId());
        PaymentMethod paymentMethod = builder.build();
        return paymentRestMapper.mapModelToResponse(paymentMethod);
    }

    @DeleteMapping("/{paymentMethodId}")
    public PaymentMethodResponse archive(@PathVariable String paymentMethodId)
            throws PaymentMethodNotFoundException, PaymentMethodMissingRequiredAttributeException,
            TraderTeamNotFoundException, PaymentMethodInvalidBankCardNumberException, BankNotFoundException,
            TerminalNotFoundException {
        PaymentMethodBuilder builder = paymentMethodService.update(paymentMethodId);
        builder.withArchived();
        PaymentMethod paymentMethod = builder.build();
        return paymentRestMapper.mapModelToResponse(paymentMethod);
    }

}
