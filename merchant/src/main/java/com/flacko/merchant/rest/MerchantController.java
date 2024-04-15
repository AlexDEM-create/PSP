package com.flacko.merchant.rest;

import com.flacko.merchant.Merchant;
import com.flacko.merchant.MerchantBuilder;
import com.flacko.merchant.MerchantService;
import com.flacko.merchant.exception.MerchantMissingRequiredAttributeException;
import com.flacko.merchant.exception.MerchantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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
                .map(merchant -> {
                    try {
                        return merchant.build();
                    } catch (MerchantMissingRequiredAttributeException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(merchantRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{merchantId}")
    public MerchantResponse get(@PathVariable String merchantId) throws MerchantNotFoundException {
        return merchantRestMapper.mapModelToResponse((Merchant) merchantService.get(merchantId));
    }

    @PostMapping
    public MerchantResponse create(@RequestBody MerchantInitiateRequest merchantInitiateRequest)
            throws MerchantMissingRequiredAttributeException {
        MerchantBuilder builder = merchantService.create();
        builder.withId(merchantInitiateRequest.id());
        if (merchantInitiateRequest.name().isPresent()) {
            builder.withName(merchantInitiateRequest.name().get());
        }
        if (merchantInitiateRequest.userId().isPresent()) {
            builder.withUserId(merchantInitiateRequest.userId().get());
        }
        Merchant merchant = builder.build();
        return merchantRestMapper.mapModelToResponse(merchant);
    }

    @DeleteMapping("/{merchantId}")
    public MerchantResponse archive(@PathVariable String merchantId)
            throws MerchantNotFoundException, MerchantMissingRequiredAttributeException {
        MerchantBuilder builder = merchantService.update(merchantId);
        builder.withArchived();
        Merchant merchant = builder.build();
        return merchantRestMapper.mapModelToResponse(merchant);
    }

}
