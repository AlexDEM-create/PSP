package com.flacko.trader.team.service;

import com.flacko.common.exception.TraderTeamNotFoundException;

import java.util.List;

public interface TraderTeamService {

    TraderTeamBuilder create();

    TraderTeamBuilder update(String id) throws TraderTeamNotFoundException;

    TraderTeamListBuilder list();

    TraderTeam get(String id) throws TraderTeamNotFoundException;

    TraderTeam getByUserId(String userId) throws TraderTeamNotFoundException;

    TraderTeam getByLeaderId(String leaderId) throws TraderTeamNotFoundException;

}
