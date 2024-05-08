package com.flacko.user.webapp.rest;

import com.flacko.user.service.UserRole;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public record UserFilterRequest(@RequestParam(BANNED) Optional<Boolean> banned,
                                @RequestParam(ROLE) Optional<UserRole> role,
                                @RequestParam(name = LIMIT, defaultValue = "10") int limit,
                                @RequestParam(name = OFFSET, defaultValue = "0") int offset) {

    private static final String BANNED = "banned";
    private static final String ROLE = "role";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

}
