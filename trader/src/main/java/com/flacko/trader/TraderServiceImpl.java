package com.flacko.trader;

import com.flacko.trader.exception.TraderNotFoundException;
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
public class TraderServiceImpl implements TraderService {
    private final TraderRepository traderRepository;
    private final ApplicationContext context;
    @Override
    public TraderBuilder create() {
        return context.getBean(TraderBuilderImpl.class)
                .initializeNew();
    }

    @Override
    public TraderBuilder update(String id) throws TraderNotFoundException {
        Trader existingTrader = (Trader)get(id);
        return context.getBean(TraderBuilderImpl.class)
                .initializeExisting(existingTrader);
    }



    @Override
    public TraderBuilder get(String id) throws TraderNotFoundException {
        return context.getBean(TraderBuilderImpl.class)
                .initializeExisting(traderRepository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new TraderNotFoundException(id)));
    }

    @Override
    public List<TraderBuilder> list() {
        return StreamSupport.stream(traderRepository.findAll().spliterator(), false)
                .map(trader -> context.getBean(TraderBuilderImpl.class)
                        .initializeExisting(trader))
                .collect(Collectors.toList());
    }
}
