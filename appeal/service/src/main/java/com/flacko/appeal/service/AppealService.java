package com.flacko.appeal.service;

import com.flacko.common.exception.AppealNotFoundException;

public interface AppealService {

    AppealBuilder create();

    AppealBuilder update(String id) throws AppealNotFoundException;

    AppealListBuilder list();

    Appeal get(String id) throws AppealNotFoundException;

}
