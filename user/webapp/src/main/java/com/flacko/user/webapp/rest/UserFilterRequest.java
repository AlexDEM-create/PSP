package com.flacko.user.webapp.rest;

import com.flacko.user.service.UserRole;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public record UserFilterRequest(@RequestParam(BANNED) Optional<Boolean> banned,
                                @RequestParam(ROLE) Optional<UserRole> role) {

    private static final String BANNED = "banned";
    private static final String ROLE = "role";

}
