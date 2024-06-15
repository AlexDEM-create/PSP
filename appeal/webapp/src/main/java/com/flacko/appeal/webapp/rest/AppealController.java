package com.flacko.appeal.webapp.rest;

import com.auth0.jwt.JWT;
import com.flacko.appeal.service.Appeal;
import com.flacko.appeal.service.AppealBuilder;
import com.flacko.appeal.service.AppealListBuilder;
import com.flacko.appeal.service.AppealService;
import com.flacko.appeal.service.AppealSource;
import com.flacko.appeal.service.AppealState;
import com.flacko.appeal.service.exception.AppealIllegalPaymentCurrentStateException;
import com.flacko.appeal.service.exception.AppealIllegalStateTransitionException;
import com.flacko.appeal.service.exception.AppealMissingRequiredAttributeException;
import com.flacko.common.exception.AppealNotFoundException;
import com.flacko.common.exception.IncomingPaymentNotFoundException;
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
import com.flacko.common.role.UserRole;
import com.flacko.security.SecurityConfig;
import com.flacko.user.service.User;
import com.flacko.user.service.UserService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/appeals")
public class AppealController {

    private static final String PAYMENT_ID = "payment_id";
    private static final String SOURCE = "source";
    private static final String CURRENT_STATE = "current_state";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final AppealService appealService;
    private final AppealRestMapper appealRestMapper;
    private final UserService userService;

    @GetMapping
    public List<AppealResponse> list(@RequestParam(PAYMENT_ID) Optional<String> paymentId,
                                     @RequestParam(SOURCE) Optional<AppealSource> source,
                                     @RequestParam(CURRENT_STATE) Optional<AppealState> currentState,
                                     @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                     @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        AppealListBuilder builder = appealService.list();
        paymentId.ifPresent(builder::withPaymentId);
        source.ifPresent(builder::withSource);
        currentState.ifPresent(builder::withCurrentState);

        List<AppealResponse> appeals = builder.build()
                .stream()
                .map(appealRestMapper::mapModelToResponse)
                .filter(appeal -> appeal.currentState() == AppealState.INITIATED)
                .sorted(Comparator.comparing(AppealResponse::createdDate).reversed())
                .collect(Collectors.toList());

        appeals.addAll(builder.build()
                .stream()
                .map(appealRestMapper::mapModelToResponse)
                .filter(appeal -> appeal.currentState() != AppealState.INITIATED)
                .sorted(Comparator.comparing(AppealResponse::createdDate).reversed())
                .collect(Collectors.toList()));

        return appeals.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping("/{appealId}")
    public AppealResponse get(@PathVariable String appealId) throws AppealNotFoundException {
        return appealRestMapper.mapModelToResponse(appealService.get(appealId));
    }

    @PostMapping
    public AppealResponse create(@RequestHeader("Authorization") String tokenWithPrefix,
                                 @RequestBody AppealCreateRequest appealCreateRequest)
            throws AppealMissingRequiredAttributeException, IncomingPaymentNotFoundException,
            AppealIllegalPaymentCurrentStateException, OutgoingPaymentNotFoundException,
            OutgoingPaymentIllegalStateTransitionException, TraderTeamNotFoundException,
            OutgoingPaymentMissingRequiredAttributeException, PaymentMethodNotFoundException,
            OutgoingPaymentInvalidAmountException, MerchantNotFoundException, NoEligibleTraderTeamsException,
            MerchantInsufficientOutgoingBalanceException, UserNotFoundException {
        String token = tokenWithPrefix.substring(SecurityConfig.TOKEN_PREFIX.length());
        String login = JWT.decode(token).getSubject();
        User user = userService.getByLogin(login);

        AppealBuilder builder = appealService.create();
        builder.withPaymentId(appealCreateRequest.paymentId());

        if (user.getRole() == UserRole.TRADER_TEAM) {
            builder.withSource(AppealSource.TRADER_TEAM);
        } else if (user.getRole() == UserRole.MERCHANT) {
            builder.withSource(AppealSource.MERCHANT);
        }

        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

    @PatchMapping("/{appealId}/resolve")
    public AppealResponse resolve(@PathVariable String appealId)
            throws AppealNotFoundException, AppealMissingRequiredAttributeException,
            AppealIllegalStateTransitionException, IncomingPaymentNotFoundException,
            AppealIllegalPaymentCurrentStateException, OutgoingPaymentNotFoundException,
            OutgoingPaymentIllegalStateTransitionException, TraderTeamNotFoundException,
            OutgoingPaymentMissingRequiredAttributeException, PaymentMethodNotFoundException,
            OutgoingPaymentInvalidAmountException, MerchantNotFoundException, NoEligibleTraderTeamsException,
            MerchantInsufficientOutgoingBalanceException {
        AppealBuilder builder = appealService.update(appealId);
        builder.withState(AppealState.RESOLVED);
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

    @PatchMapping("/{appealId}/reject")
    public AppealResponse reject(@PathVariable String appealId)
            throws AppealNotFoundException, AppealMissingRequiredAttributeException,
            AppealIllegalStateTransitionException, IncomingPaymentNotFoundException,
            AppealIllegalPaymentCurrentStateException, OutgoingPaymentNotFoundException,
            OutgoingPaymentIllegalStateTransitionException, TraderTeamNotFoundException,
            OutgoingPaymentMissingRequiredAttributeException, PaymentMethodNotFoundException,
            OutgoingPaymentInvalidAmountException, MerchantNotFoundException, NoEligibleTraderTeamsException,
            MerchantInsufficientOutgoingBalanceException {
        AppealBuilder builder = appealService.update(appealId);
        builder.withState(AppealState.REJECTED);
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

    @PatchMapping("/{appealId}/review")
    public AppealResponse review(@PathVariable String appealId)
            throws AppealNotFoundException, AppealMissingRequiredAttributeException,
            AppealIllegalStateTransitionException, IncomingPaymentNotFoundException,
            AppealIllegalPaymentCurrentStateException, OutgoingPaymentNotFoundException,
            OutgoingPaymentIllegalStateTransitionException, TraderTeamNotFoundException,
            OutgoingPaymentMissingRequiredAttributeException, PaymentMethodNotFoundException,
            OutgoingPaymentInvalidAmountException, MerchantNotFoundException, NoEligibleTraderTeamsException,
            MerchantInsufficientOutgoingBalanceException {
        AppealBuilder builder = appealService.update(appealId);
        builder.withState(AppealState.UNDER_REVIEW);
        Appeal appeal = builder.build();
        return appealRestMapper.mapModelToResponse(appeal);
    }

}
