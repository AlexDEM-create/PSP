package com.flacko.outgoing.payment.impl;

import com.flacko.common.exception.PaymentNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.outgoing.payment.service.OutgoingPayment;
import com.flacko.outgoing.payment.service.OutgoingPaymentBuilder;
import com.flacko.outgoing.payment.service.OutgoingPaymentListBuilder;
import com.flacko.outgoing.payment.service.OutgoingPaymentService;
import com.flacko.payment.service.Payment;
import com.flacko.payment.service.OutgoingPaymentBuilder;
import com.flacko.payment.service.PaymentListBuilder;
import com.flacko.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutgoingPaymentServiceImpl implements OutgoingPaymentService {

    private final OutgoingPaymentRepository outgoingPaymentRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public OutgoingPaymentListBuilder list() {
        return serviceLocator.create(OutgoingPaymentListBuilder.class);
    }

    @Override
    public OutgoingPayment get(String id) throws PaymentNotFoundException {
        return (OutgoingPayment) outgoingPaymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Transactional
    @Override
    public OutgoingPaymentBuilder create() {
        return serviceLocator.create(InitializableOutgoingPaymentBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public OutgoingPaymentBuilder update(String id) throws PaymentNotFoundException {
        OutgoingPayment existingPayment = get(id);
        return serviceLocator.create(InitializableOutgoingPaymentBuilder.class)
                .initializeExisting(existingPayment);
    }

}
