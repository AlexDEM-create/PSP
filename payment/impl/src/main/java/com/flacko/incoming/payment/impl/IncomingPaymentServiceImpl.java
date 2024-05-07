package com.flacko.incoming.payment.impl;

import com.flacko.common.exception.PaymentNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.payment.service.Payment;
import com.flacko.payment.service.OutgoingPaymentBuilder;
import com.flacko.payment.service.PaymentListBuilder;
import com.flacko.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IncomingPaymentServiceImpl implements PaymentService {

    private final IncomingPaymentRepository incomingPaymentRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public PaymentListBuilder list() {
        return serviceLocator.create(PaymentListBuilder.class);
    }

    @Override
    public Payment get(String id) throws PaymentNotFoundException {
        return incomingPaymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Transactional
    @Override
    public OutgoingPaymentBuilder create() {
        return serviceLocator.create(InitializableIncomingPaymentBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public OutgoingPaymentBuilder update(String id) throws PaymentNotFoundException {
        Payment existingPayment = get(id);
        return serviceLocator.create(InitializableIncomingPaymentBuilder.class)
                .initializeExisting(existingPayment);
    }

}
