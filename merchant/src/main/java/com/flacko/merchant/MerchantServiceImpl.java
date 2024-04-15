package com.flacko.merchant;


import com.flacko.merchant.exception.MerchantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;
    private final ApplicationContext context;
    @Override
    public MerchantBuilder create() {
        return context.getBean(MerchantBuilderImpl.class)
                .initializeNew();
    }

    @Override
    public MerchantBuilder update(String id) throws MerchantNotFoundException {
        Merchant existingMerchant = (Merchant)get(id);
        return context.getBean(MerchantBuilderImpl.class)
                .initializeExisting(existingMerchant);
    }

    @Override
    public MerchantBuilder get(String id) throws MerchantNotFoundException {
        return context.getBean(MerchantBuilderImpl.class)
                .initializeExisting(merchantRepository.findById(id)
                        .orElseThrow(() -> new MerchantNotFoundException(id)));
    }

    @Override
    public List<MerchantBuilder> list() {
        return StreamSupport.stream(merchantRepository.findAll().spliterator(), false)
                .map(merchant -> context.getBean(MerchantBuilderImpl.class)
                        .initializeExisting(merchant))
                .collect(Collectors.toList());
    }
}