package com.flacko.merchant;


import com.flacko.merchant.exception.MerchantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;

    @Override
    public Merchant create(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    @Override
    public Merchant update(int merchantId, Merchant merchant) {
        Merchant existingMerchant = get(merchantId);
        existingMerchant.setName(merchant.getName());
        return merchantRepository.save(existingMerchant);
    }

    @Override
    public Merchant get(String id) {
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new MerchantNotFoundException(merchantId));
    }

    @Override
    public List<Merchant> list() {
        return StreamSupport.stream(merchantRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {

    }
}
