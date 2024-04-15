package com.flacko.trader;

import com.flacko.trader.exception.TraderNotFoundException;

import java.util.List;

public interface TraderService {

    TraderBuilder create();
    TraderBuilder update(String id) throws TraderNotFoundException;
    TraderBuilder get(String id) throws TraderNotFoundException;
    List<TraderBuilder> list();


}