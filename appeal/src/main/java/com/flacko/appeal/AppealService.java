package com.flacko.appeal;

import com.flacko.appeal.exception.AppealNotFoundException;



public interface AppealService {

    AppealBuilder create();

    Appeal get(String id) throws AppealNotFoundException;

}
