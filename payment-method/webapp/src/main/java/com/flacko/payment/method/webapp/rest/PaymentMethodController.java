package com.flacko.payment.method.webapp.rest;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.method.service.PaymentMethodBuilder;
import com.flacko.payment.method.service.PaymentMethodListBuilder;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.method.service.exception.PaymentMethodInvalidBankAccountLastFourDigitsException;
import com.flacko.payment.method.service.exception.PaymentMethodInvalidBankCardNumberException;
import com.flacko.payment.method.service.exception.PaymentMethodMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
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
    private static final String ENABLED = "enabled";
    private static final String BUSY = "busy";
    private static final String ARCHIVED = "archived";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final PaymentMethodService paymentMethodService;
    private final PaymentRestMapper paymentRestMapper;

    @GetMapping
    public List<PaymentMethodResponse> list(@RequestParam(CURRENCY) Optional<Currency> currency,
                                            @RequestParam(BANK) Optional<Bank> bank,
                                            @RequestParam(TRADER_TEAM_ID) Optional<String> traderTeamId,
                                            @RequestParam(TERMINAL_ID) Optional<String> terminalId,
                                            @RequestParam(ENABLED) Optional<Boolean> enabled,
                                            @RequestParam(BUSY) Optional<Boolean> busy,
                                            @RequestParam(ARCHIVED) Optional<Boolean> archived,
                                            @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                            @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        PaymentMethodListBuilder builder = paymentMethodService.list();
        currency.ifPresent(builder::withCurrency);
        bank.ifPresent(builder::withBank);
        traderTeamId.ifPresent(builder::withTraderTeamId);
        terminalId.ifPresent(builder::withTerminalId);
        enabled.ifPresent(builder::withEnabled);
        busy.ifPresent(builder::withBusy);
        archived.ifPresent(builder::withArchived);
        return builder.build()
                .stream()
                .map(paymentRestMapper::mapModelToResponse)
                .skip(offset)
                .limit(limit)
                .sorted(Comparator.comparing(PaymentMethodResponse::createdDate).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("/{paymentMethodId}")
    public PaymentMethodResponse get(@PathVariable String paymentMethodId) throws PaymentMethodNotFoundException {
        return paymentRestMapper.mapModelToResponse(paymentMethodService.get(paymentMethodId));
    }

    @PostMapping
    public PaymentMethodResponse create(@RequestBody PaymentMethodCreateRequest paymentMethodCreateRequest)
            throws PaymentMethodMissingRequiredAttributeException, TraderTeamNotFoundException,
            PaymentMethodInvalidBankCardNumberException, TerminalNotFoundException,
            PaymentMethodInvalidBankAccountLastFourDigitsException {
        PaymentMethodBuilder builder = paymentMethodService.create();
        builder.withNumber(paymentMethodCreateRequest.number())
                .withAccountLastFourDigits(paymentMethodCreateRequest.accountLastFourDigits())
                .withFirstName(paymentMethodCreateRequest.firstName())
                .withLastName(paymentMethodCreateRequest.lastName())
                .withCurrency(paymentMethodCreateRequest.currency())
                .withBank(paymentMethodCreateRequest.bank())
                .withTraderTeamId(paymentMethodCreateRequest.traderTeamId());
        paymentMethodCreateRequest.terminalId().ifPresent(builder::withTerminalId);
        PaymentMethod paymentMethod = builder.build();
        return paymentRestMapper.mapModelToResponse(paymentMethod);
    }

    @DeleteMapping("/{paymentMethodId}")
    public PaymentMethodResponse archive(@PathVariable String paymentMethodId)
            throws PaymentMethodNotFoundException, PaymentMethodMissingRequiredAttributeException,
            TraderTeamNotFoundException, PaymentMethodInvalidBankCardNumberException,
            TerminalNotFoundException, PaymentMethodInvalidBankAccountLastFourDigitsException {
        PaymentMethodBuilder builder = paymentMethodService.update(paymentMethodId);
        builder.withArchived();
        PaymentMethod paymentMethod = builder.build();
        return paymentRestMapper.mapModelToResponse(paymentMethod);
    }

    @PatchMapping("/{paymentMethodId}/enable")
    public PaymentMethodResponse enable(@PathVariable String paymentMethodId) throws PaymentMethodNotFoundException,
            TraderTeamNotFoundException, PaymentMethodMissingRequiredAttributeException, TerminalNotFoundException,
            PaymentMethodInvalidBankCardNumberException, PaymentMethodInvalidBankAccountLastFourDigitsException {
        PaymentMethodBuilder builder = paymentMethodService.update(paymentMethodId);
        builder.withEnabled(true);
        PaymentMethod paymentMethod = builder.build();
        return paymentRestMapper.mapModelToResponse(paymentMethod);
    }

    @PatchMapping("/{paymentMethodId}/disable")
    public PaymentMethodResponse disable(@PathVariable String paymentMethodId) throws PaymentMethodNotFoundException,
            TraderTeamNotFoundException, PaymentMethodMissingRequiredAttributeException, TerminalNotFoundException,
            PaymentMethodInvalidBankCardNumberException, PaymentMethodInvalidBankAccountLastFourDigitsException {
        PaymentMethodBuilder builder = paymentMethodService.update(paymentMethodId);
        builder.withEnabled(false);
        PaymentMethod paymentMethod = builder.build();
        return paymentRestMapper.mapModelToResponse(paymentMethod);
    }

}
