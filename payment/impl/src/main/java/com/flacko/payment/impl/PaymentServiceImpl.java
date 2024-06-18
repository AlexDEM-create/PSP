package com.flacko.payment.impl;

import com.flacko.common.spring.ServiceLocator;
import com.flacko.payment.service.PaymentListBuilder;
import com.flacko.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ServiceLocator serviceLocator;

    @Override
    public PaymentListBuilder list() {
        return serviceLocator.create(PaymentListBuilder.class);
    }

}
