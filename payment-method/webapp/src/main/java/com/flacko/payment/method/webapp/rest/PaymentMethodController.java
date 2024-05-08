package com.flacko.payment.method.webapp.rest;

import com.flacko.common.currency.Currency;
import com.flacko.common.exception.BankNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.payment.method.service.*;
import com.flacko.payment.method.service.exception.PaymentMethodInvalidBankCardNumberException;
import com.flacko.payment.method.service.exception.PaymentMethodInvalidPhoneNumberException;
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

    private static final String TYPE = "type";
    private static final String CURRENCY = "currency";
    private static final String BANK_ID = "bank_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String TERMINAL_ID = "terminal_id";
    private static final String BUSY = "busy";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final PaymentMethodService paymentMethodService;
    private final PaymentRestMapper paymentRestMapper;

    @GetMapping
    public List<PaymentMethodResponse> list(@RequestParam(TYPE) Optional<PaymentMethodType> type,
                                            @RequestParam(CURRENCY) Optional<Currency> currency,
                                            @RequestParam(BANK_ID) Optional<String> bankId,
                                            @RequestParam(TRADER_TEAM_ID) Optional<String> traderTeamId,
                                            @RequestParam(TERMINAL_ID) Optional<String> terminalId,
                                            @RequestParam(BUSY) Optional<Boolean> busy,
                                            @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                            @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        PaymentMethodListBuilder builder = paymentMethodService.list();
        type.ifPresent(builder::withType);
        currency.ifPresent(builder::withCurrency);
        bankId.ifPresent(builder::withBankId);
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
            PaymentMethodInvalidBankCardNumberException, BankNotFoundException, TerminalNotFoundException,
            PaymentMethodInvalidPhoneNumberException {
        PaymentMethodBuilder builder = paymentMethodService.create();
        builder.withType(paymentMethodCreateRequest.type())
                .withNumber(paymentMethodCreateRequest.number())
                .withHolderName(paymentMethodCreateRequest.holderName())
                .withCurrency(paymentMethodCreateRequest.currency())
                .withBankId(paymentMethodCreateRequest.bankId())
                .withTraderTeamId(paymentMethodCreateRequest.traderTeamId())
                .withTerminalId(paymentMethodCreateRequest.terminalId());
        PaymentMethod paymentMethod = builder.build();
        return paymentRestMapper.mapModelToResponse(paymentMethod);
    }

    @DeleteMapping("/{paymentMethodId}")
    public PaymentMethodResponse archive(@PathVariable String paymentMethodId)
            throws PaymentMethodNotFoundException, PaymentMethodMissingRequiredAttributeException,
            TraderTeamNotFoundException, PaymentMethodInvalidBankCardNumberException, BankNotFoundException,
            TerminalNotFoundException, PaymentMethodInvalidPhoneNumberException {
        PaymentMethodBuilder builder = paymentMethodService.update(paymentMethodId);
        builder.withArchived();
        PaymentMethod paymentMethod = builder.build();
        return paymentRestMapper.mapModelToResponse(paymentMethod);
    }

}
