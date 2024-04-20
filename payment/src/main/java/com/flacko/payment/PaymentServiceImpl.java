package com.flacko.payment;

import com.flacko.auth.spring.ServiceLocator;
import com.flacko.payment.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public List<Payment> list() {
        return StreamSupport.stream(paymentRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Payment get(String id) throws PaymentNotFoundException {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Override
    public PaymentBuilder create() {
        return serviceLocator.create(InitializablePaymentBuilder.class)
                .initializeNew();
    }

    @Override
    public PaymentBuilder update(String id) throws PaymentNotFoundException {
        Payment existingPayment = get(id);
        return serviceLocator.create(InitializablePaymentBuilder.class)
                .initializeExisting(existingPayment);
    }

}
