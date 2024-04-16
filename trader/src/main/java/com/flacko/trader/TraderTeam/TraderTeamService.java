package com.flacko.trader.TraderTeam;

import com.flacko.trader.TraderTeam.exception.TraderTeamNotFoundException;

import java.util.List;

public interface TraderTeamService {

    TraderTeamBuilder create();
    TraderTeamBuilder update(String id) throws TraderTeamNotFoundException;
    TraderTeamBuilder get(String id) throws TraderTeamNotFoundException;
    List<TraderTeamBuilder> list();
}

