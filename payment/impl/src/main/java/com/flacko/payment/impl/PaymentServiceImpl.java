package com.flacko.payment.impl;

import com.flacko.common.exception.PaymentNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.payment.service.Payment;
import com.flacko.payment.service.PaymentBuilder;
import com.flacko.payment.service.PaymentListBuilder;
import com.flacko.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public PaymentListBuilder list() {
        return serviceLocator.create(PaymentListBuilder.class);
    }

    @Override
    public Payment get(String id) throws PaymentNotFoundException {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Transactional
    @Override
    public PaymentBuilder create() {
        return serviceLocator.create(InitializablePaymentBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public PaymentBuilder update(String id) throws PaymentNotFoundException {
        Payment existingPayment = get(id);
        return serviceLocator.create(InitializablePaymentBuilder.class)
                .initializeExisting(existingPayment);
    }

}
