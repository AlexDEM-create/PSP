package com.flacko.trader;

import com.flacko.trader.exception.TraderNotFoundException;
import lombok.RequiredArgsConstructor;
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

    @Override
    public Trader create(Trader trader) {
        return traderRepository.save(trader);
    }

    @Override
    public Trader update(String id, Trader trader) throws TraderNotFoundException {
        Trader existingTrader = get(id);
        existingTrader.setName(trader.getName());
        return traderRepository.save(existingTrader);
    }

    @Override
    public Trader get(String id) throws TraderNotFoundException {
        return traderRepository.findById(id)
                .orElseThrow(() -> new TraderNotFoundException(id));
    }

    @Override
    public List<Trader> list() {
        return StreamSupport.stream(traderRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        traderRepository.deleteById(id);
    }
}
