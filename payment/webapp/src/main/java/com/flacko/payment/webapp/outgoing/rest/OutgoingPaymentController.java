package com.flacko.payment.webapp.outgoing.rest;

import com.auth0.jwt.JWT;
import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.MerchantInsufficientOutgoingBalanceException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.NoEligibleTraderTeamsException;
import com.flacko.common.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.common.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.common.exception.OutgoingPaymentMissingRequiredAttributeException;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentBuilder;
import com.flacko.payment.service.outgoing.OutgoingPaymentListBuilder;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
                                              @RequestParam(RECIPIENT_PAYMENT_METHOD_TYPE) Optional<RecipientPaymentMethodType> recipientPaymentMethodType,
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

        List<OutgoingPaymentResponse> payments = builder.build()
                .stream()
                .map(outgoingPaymentRestMapper::mapModelToResponse)
                .collect(Collectors.toList());

        List<OutgoingPaymentResponse> initiatedPayments = payments.stream()
                .filter(payment -> payment.currentState() == PaymentState.INITIATED)
                .sorted(Comparator.comparing(OutgoingPaymentResponse::createdDate).reversed())
                .collect(Collectors.toList());

        List<OutgoingPaymentResponse> otherPayments = payments.stream()
                .filter(payment -> payment.currentState() != PaymentState.INITIATED)
                .sorted(Comparator.comparing(OutgoingPaymentResponse::createdDate).reversed())
                .collect(Collectors.toList());

        List<OutgoingPaymentResponse> combinedPayments = Stream.concat(initiatedPayments.stream(),
                otherPayments.stream())
                .collect(Collectors.toList());

        return combinedPayments.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }


    @GetMapping("/{outgoingPaymentId}")
    public OutgoingPaymentResponse get(@PathVariable String outgoingPaymentId) throws OutgoingPaymentNotFoundException {
        return outgoingPaymentRestMapper.mapModelToResponse(outgoingPaymentService.get(outgoingPaymentId));
    }

    @PostMapping
    public OutgoingPaymentCreateResponse create(@RequestHeader("Authorization") String tokenWithPrefix,
                                                @RequestBody OutgoingPaymentCreateRequest outgoingPaymentCreateRequest)
            throws TraderTeamNotFoundException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, MerchantNotFoundException,
            UserNotFoundException, NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException {
        String token = tokenWithPrefix.substring(SecurityConfig.TOKEN_PREFIX.length());
        String login = JWT.decode(token).getSubject();

        OutgoingPaymentBuilder builder = outgoingPaymentService.create(login);
        builder.withAmount(outgoingPaymentCreateRequest.amount());
        builder.withCurrency(outgoingPaymentCreateRequest.currency());
        builder.withRecipient(outgoingPaymentCreateRequest.recipient());
        builder.withBank(outgoingPaymentCreateRequest.bank());
        builder.withRecipientPaymentMethodType(outgoingPaymentCreateRequest.recipientPaymentMethodType());

        if (outgoingPaymentCreateRequest.partnerPaymentId().isPresent()) {
            builder.withPartnerPaymentId(outgoingPaymentCreateRequest.partnerPaymentId().get());
        }

        builder.withRandomTraderTeamId(Optional.empty());
        OutgoingPayment outgoingPayment = builder.build();
        return outgoingPaymentRestMapper.mapModelToCreateResponse(outgoingPayment);
    }

    @PostMapping("/test")
    public OutgoingPaymentCreateResponse create(@RequestHeader("Authorization") String tokenWithPrefix,
                                                @RequestBody TestOutgoingPaymentCreateRequest testOutgoingPaymentCreateRequest)
            throws TraderTeamNotFoundException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, MerchantNotFoundException,
            UserNotFoundException, NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException {
        String token = tokenWithPrefix.substring(SecurityConfig.TOKEN_PREFIX.length());
        String login = JWT.decode(token).getSubject();

        OutgoingPaymentBuilder builder = outgoingPaymentService.create(login);
        builder.withAmount(testOutgoingPaymentCreateRequest.amount());
        builder.withCurrency(testOutgoingPaymentCreateRequest.currency());
        builder.withRecipient(testOutgoingPaymentCreateRequest.recipient());
        builder.withBank(testOutgoingPaymentCreateRequest.bank());
        builder.withRecipientPaymentMethodType(testOutgoingPaymentCreateRequest.recipientPaymentMethodType());
        builder.withTraderTeamId(testOutgoingPaymentCreateRequest.traderTeamId());
        builder.withMerchantId(testOutgoingPaymentCreateRequest.merchantId());

        if (testOutgoingPaymentCreateRequest.partnerPaymentId().isPresent()) {
            builder.withPartnerPaymentId(testOutgoingPaymentCreateRequest.partnerPaymentId().get());
        }

        OutgoingPayment outgoingPayment = builder.build();
        return outgoingPaymentRestMapper.mapModelToCreateResponse(outgoingPayment);
    }

    @PatchMapping("/{outgoingPaymentId}/verify")
    public OutgoingPaymentCreateResponse verify(@PathVariable String outgoingPaymentId)
            throws OutgoingPaymentNotFoundException, OutgoingPaymentIllegalStateTransitionException,
            MerchantInsufficientOutgoingBalanceException, TraderTeamNotFoundException,
            OutgoingPaymentMissingRequiredAttributeException, PaymentMethodNotFoundException,
            OutgoingPaymentInvalidAmountException, MerchantNotFoundException, UserNotFoundException {
        OutgoingPaymentBuilder builder = outgoingPaymentService.update(outgoingPaymentId);
        builder.withState(PaymentState.VERIFYING);
        OutgoingPayment outgoingPayment = builder.build();
        return outgoingPaymentRestMapper.mapModelToCreateResponse(outgoingPayment);
    }

    @PatchMapping("/{outgoingPaymentId}/reassign")
    public OutgoingPaymentResponse reassign(@RequestHeader("Authorization") String tokenWithPrefix,
                                            @PathVariable String outgoingPaymentId)
            throws OutgoingPaymentIllegalStateTransitionException, TraderTeamNotFoundException,
            OutgoingPaymentMissingRequiredAttributeException, PaymentMethodNotFoundException,
            OutgoingPaymentInvalidAmountException, MerchantNotFoundException, OutgoingPaymentNotFoundException,
            NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException, UserNotFoundException {
        String token = tokenWithPrefix.substring(SecurityConfig.TOKEN_PREFIX.length());
        String login = JWT.decode(token).getSubject();

        OutgoingPayment outgoingPayment = outgoingPaymentService.reassignRandomTraderTeam(outgoingPaymentId, login);
        return outgoingPaymentRestMapper.mapModelToResponse(outgoingPayment);
    }

}
