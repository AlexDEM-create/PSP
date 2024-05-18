package service.exception;

import com.flacko.common.exception.NotFoundException;

public class StatsNotFoundException extends NotFoundException {

    public StatsNotFoundException(String id) {
        super(String.format("Stats %s not found", id));
    }

}
