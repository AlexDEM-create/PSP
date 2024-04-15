package com.flacko.trader;

import com.flacko.trader.exception.TraderNotFoundException;

import java.util.List;

public interface TraderService {
    Trader create(Trader trader);
    Trader update(String id, Trader trader) throws TraderNotFoundException;
    Trader get(String id) throws TraderNotFoundException;
    List<Trader> list();
}