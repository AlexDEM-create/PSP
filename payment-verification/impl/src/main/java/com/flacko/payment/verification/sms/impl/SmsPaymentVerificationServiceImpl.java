package com.flacko.payment.verification.sms.impl;

import com.flacko.common.exception.SmsPaymentVerificationNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.payment.verification.sms.service.SmsPaymentVerification;
import com.flacko.payment.verification.sms.service.SmsPaymentVerificationBuilder;
import com.flacko.payment.verification.sms.service.SmsPaymentVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SmsPaymentVerificationServiceImpl implements SmsPaymentVerificationService {

    private final SmsPaymentVerificationRepository smsPaymentVerificationRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public List<SmsPaymentVerification> list() {
        return StreamSupport.stream(smsPaymentVerificationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public SmsPaymentVerification get(String id) throws SmsPaymentVerificationNotFoundException {
        return smsPaymentVerificationRepository.findById(id)
                .orElseThrow(() -> new SmsPaymentVerificationNotFoundException(id));
    }

    @Override
    @Transactional
    public SmsPaymentVerificationBuilder create() {
        return serviceLocator.create(InitializableSmsPaymentVerificationBuilder.class)
                .initializeNew();
    }

}
