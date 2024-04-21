package com.flacko.merchant.rest;

import com.flacko.auth.security.user.exception.UserNotFoundException;
import com.flacko.merchant.Merchant;
import com.flacko.merchant.MerchantBuilder;
import com.flacko.merchant.MerchantService;
import com.flacko.merchant.exception.MerchantInvalidFeeRateException;
import com.flacko.merchant.exception.MerchantMissingRequiredAttributeException;
import com.flacko.merchant.exception.MerchantNotFoundException;
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
            MerchantInvalidFeeRateException {
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
            UserNotFoundException, MerchantInvalidFeeRateException {
        MerchantBuilder builder = merchantService.update(merchantId);
        builder.withArchived();
        Merchant merchant = builder.build();
        return merchantRestMapper.mapModelToResponse(merchant);
    }

}
