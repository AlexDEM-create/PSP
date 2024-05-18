package com.flacko.merchant.impl;


import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantBuilder;
import com.flacko.merchant.service.MerchantListBuilder;
import com.flacko.merchant.service.MerchantService;
import com.flacko.user.service.User;
import com.flacko.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final ServiceLocator serviceLocator;
    private final UserService userService;

    @Override
    public MerchantListBuilder list() {
        return serviceLocator.create(MerchantListBuilder.class);
    }

    @Override
    public Merchant get(String id) throws MerchantNotFoundException {
        return merchantRepository.findById(id)
                .orElseThrow(() -> new MerchantNotFoundException(id));
    }

    @Override
    public Merchant getByUserId(String userId) throws MerchantNotFoundException {
        return merchantRepository.findByUserId(userId)
                .orElseThrow(() -> new MerchantNotFoundException("user", userId));
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

    @Override
    public Merchant getMy(String login) throws MerchantNotFoundException, UserNotFoundException {
        User user = userService.getByLogin(login);
        return getByUserId(user.getId());
    }

}
