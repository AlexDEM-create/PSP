package com.flacko.merchant.webapp.rest;

import com.flacko.common.country.Country;
import com.flacko.common.exception.BalanceInvalidCurrentBalanceException;
import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantInvalidFeeRateException;
import com.flacko.common.exception.MerchantMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantBuilder;
import com.flacko.merchant.service.MerchantListBuilder;
import com.flacko.merchant.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchants")
public class MerchantController {

    private static final String USER_ID = "user_id";
    private static final String COUNTRY = "country";
    private static final String OUTGOING_TRAFFIC_STOPPED = "outgoing_traffic_stopped";
    private static final String ARCHIVED = "archived";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final MerchantService merchantService;
    private final MerchantRestMapper merchantRestMapper;

    @GetMapping
    public List<MerchantResponse> list(@RequestParam(USER_ID) Optional<String> userId,
                                       @RequestParam(COUNTRY) Optional<Country> country,
                                       @RequestParam(OUTGOING_TRAFFIC_STOPPED) Optional<Boolean> outgoingTrafficStopped,
                                       @RequestParam(ARCHIVED) Optional<Boolean> archived,
                                       @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                       @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        MerchantListBuilder builder = merchantService.list();
        userId.ifPresent(builder::withUserId);
        country.ifPresent(builder::withCountry);
        outgoingTrafficStopped.ifPresent(builder::withOutgoingTrafficStopped);
        archived.ifPresent(builder::withArchived);
        return builder.build()
                .stream()
                .map(merchantRestMapper::mapModelToResponse)
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping("/{merchantId}")
    public MerchantResponse get(@PathVariable String merchantId) throws MerchantNotFoundException {
        return merchantRestMapper.mapModelToResponse(merchantService.get(merchantId));
    }

    @PostMapping
    public MerchantResponse create(@RequestBody MerchantCreateRequest merchantCreateRequest)
            throws MerchantMissingRequiredAttributeException, UserNotFoundException,
            MerchantInvalidFeeRateException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, BalanceInvalidCurrentBalanceException {
        MerchantBuilder builder = merchantService.create();
        builder.withName(merchantCreateRequest.name())
                .withUserId(merchantCreateRequest.userId())
                .withCountry(merchantCreateRequest.country())
                .withIncomingFeeRate(merchantCreateRequest.incomingFeeRate())
                .withOutgoingFeeRate(merchantCreateRequest.outgoingFeeRate());
        Merchant merchant = builder.build();
        return merchantRestMapper.mapModelToResponse(merchant);
    }

    @DeleteMapping("/{merchantId}")
    public MerchantResponse archive(@PathVariable String merchantId)
            throws MerchantNotFoundException, MerchantMissingRequiredAttributeException,
            UserNotFoundException, MerchantInvalidFeeRateException, TraderTeamNotFoundException,
            BalanceMissingRequiredAttributeException, BalanceInvalidCurrentBalanceException {
        MerchantBuilder builder = merchantService.update(merchantId);
        builder.withArchived();
        Merchant merchant = builder.build();
        return merchantRestMapper.mapModelToResponse(merchant);
    }

}
