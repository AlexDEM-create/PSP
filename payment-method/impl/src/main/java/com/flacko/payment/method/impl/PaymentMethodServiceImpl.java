package com.flacko.payment.method.impl;

import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.method.service.PaymentMethodBuilder;
import com.flacko.payment.method.service.PaymentMethodListBuilder;
import com.flacko.payment.method.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public PaymentMethodListBuilder list() {
        return serviceLocator.create(PaymentMethodListBuilder.class);
    }

    @Override
    public PaymentMethod get(String id) throws PaymentMethodNotFoundException {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new PaymentMethodNotFoundException(id));
    }

    @Transactional
    @Override
    public PaymentMethodBuilder create() {
        return serviceLocator.create(InitializablePaymentMethodBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public PaymentMethodBuilder update(String id) throws PaymentMethodNotFoundException {
        PaymentMethod existingPaymentMethod = get(id);
        return serviceLocator.create(InitializablePaymentMethodBuilder.class)
                .initializeExisting(existingPaymentMethod);
    }

}
