package com.flacko.trader.team.service;

import com.flacko.common.exception.TraderTeamNotFoundException;

import java.util.List;

public interface TraderTeamService {

    TraderTeamBuilder create();

    TraderTeamBuilder update(String id) throws TraderTeamNotFoundException;

    List<TraderTeam> list();

    TraderTeam get(String id) throws TraderTeamNotFoundException;
}
