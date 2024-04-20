package com.flacko.appeal;

import com.flacko.appeal.exception.AppealNotFoundException;

import java.util.List;

public interface AppealService {

    AppealBuilder create();

    AppealBuilder update(String id) throws AppealNotFoundException;

    List<Appeal> list();

    Appeal get(String id) throws AppealNotFoundException;

}
