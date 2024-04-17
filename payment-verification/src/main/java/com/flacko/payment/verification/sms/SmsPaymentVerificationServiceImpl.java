package com.flacko.payment.verification.sms;

import com.flacko.payment.verification.sms.exception.SmsPaymentVerificationNotFoundException;
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
public class SmsPaymentVerificationServiceImpl implements SmsPaymentVerificationService {

    private final SmsPaymentVerificationRepository smsPaymentVerificationRepository;
    private final ApplicationContext context;

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
    public SmsPaymentVerificationBuilder create() {
        return context.getBean(SmsPaymentVerificationBuilderImpl.class)
                .initializeNew();
    }

}
