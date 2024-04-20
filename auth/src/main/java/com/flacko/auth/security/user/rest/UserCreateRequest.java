package com.flacko.auth.security.user.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.auth.security.user.UserRole;

public record UserCreateRequest(@JsonProperty(LOGIN) String login,
                                @JsonProperty(PASSWORD) String password,
                                @JsonProperty(ROLE) UserRole role) {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";

}
