package com.flacko.payment.impl.incoming;

import com.flacko.common.exception.IncomingPaymentNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.payment.service.incoming.IncomingPayment;
import com.flacko.payment.service.incoming.IncomingPaymentBuilder;
import com.flacko.payment.service.incoming.IncomingPaymentListBuilder;
import com.flacko.payment.service.incoming.IncomingPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IncomingPaymentServiceImpl implements IncomingPaymentService {

    private final IncomingPaymentRepository incomingPaymentRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public IncomingPaymentListBuilder list() {
        return serviceLocator.create(IncomingPaymentListBuilder.class);
    }

    @Override
    public IncomingPayment get(String id) throws IncomingPaymentNotFoundException {
        return incomingPaymentRepository.findById(id)
                .orElseThrow(() -> new IncomingPaymentNotFoundException(id));
    }

    @Transactional
    @Override
    public IncomingPaymentBuilder create() {
        return serviceLocator.create(InitializableIncomingPaymentBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public IncomingPaymentBuilder update(String id) throws IncomingPaymentNotFoundException {
        IncomingPayment existingIncomingPayment = get(id);
        return serviceLocator.create(InitializableIncomingPaymentBuilder.class)
                .initializeExisting(existingIncomingPayment);
    }

}
