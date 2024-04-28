package com.flacko.merchant.impl;


import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantBuilder;
import com.flacko.merchant.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public List<Merchant> list() {
        return StreamSupport.stream(merchantRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Merchant get(String id) throws MerchantNotFoundException {
        return merchantRepository.findById(id)
                .orElseThrow(() -> new MerchantNotFoundException(id));
    }

    @Transactional
    @Override
    public MerchantBuilder create() {
        return serviceLocator.create(InitializableMerchantBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public MerchantBuilder update(String id) throws MerchantNotFoundException {
        Merchant existingMerchant = get(id);
        return serviceLocator.create(InitializableMerchantBuilder.class)
                .initializeExisting(existingMerchant);
    }

}
