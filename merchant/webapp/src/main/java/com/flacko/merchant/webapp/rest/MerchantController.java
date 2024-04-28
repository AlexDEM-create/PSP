package com.flacko.merchant.webapp.rest;

import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantBuilder;
import com.flacko.merchant.service.MerchantService;
import com.flacko.merchant.service.exception.MerchantInvalidFeeRateException;
import com.flacko.merchant.service.exception.MerchantMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchants")
public class MerchantController {

    private final MerchantService merchantService;
    private final MerchantRestMapper merchantRestMapper;

    @GetMapping
    public List<MerchantResponse> list() {
        return merchantService.list()
                .stream()
                .map(merchantRestMapper::mapModelToResponse)
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
            BalanceMissingRequiredAttributeException {
        MerchantBuilder builder = merchantService.create();
        builder.withName(merchantCreateRequest.name())
                .withUserId(merchantCreateRequest.userId())
                .withIncomingFeeRate(merchantCreateRequest.incomingFeeRate())
                .withOutgoingFeeRate(merchantCreateRequest.outgoingFeeRate());
        Merchant merchant = builder.build();
        return merchantRestMapper.mapModelToResponse(merchant);
    }

    @DeleteMapping("/{merchantId}")
    public MerchantResponse archive(@PathVariable String merchantId)
            throws MerchantNotFoundException, MerchantMissingRequiredAttributeException,
            UserNotFoundException, MerchantInvalidFeeRateException, TraderTeamNotFoundException,
            BalanceMissingRequiredAttributeException {
        MerchantBuilder builder = merchantService.update(merchantId);
        builder.withArchived();
        Merchant merchant = builder.build();
        return merchantRestMapper.mapModelToResponse(merchant);
    }

}
