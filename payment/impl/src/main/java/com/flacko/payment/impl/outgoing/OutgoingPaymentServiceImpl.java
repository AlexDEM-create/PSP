package com.flacko.payment.impl.outgoing;

import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentBuilder;
import com.flacko.payment.service.outgoing.OutgoingPaymentListBuilder;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
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
    public OutgoingPayment get(String id) throws OutgoingPaymentNotFoundException {
        return outgoingPaymentRepository.findById(id)
                .orElseThrow(() -> new OutgoingPaymentNotFoundException(id));
    }

    @Transactional
    @Override
    public OutgoingPaymentBuilder create() {
        return serviceLocator.create(InitializableOutgoingPaymentBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public OutgoingPaymentBuilder update(String id) throws OutgoingPaymentNotFoundException {
        OutgoingPayment existingOutgoingPayment = get(id);
        return serviceLocator.create(InitializableOutgoingPaymentBuilder.class)
                .initializeExisting(existingOutgoingPayment);
    }

}
