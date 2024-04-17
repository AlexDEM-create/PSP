package com.flacko.auth.user.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.auth.user.Role;

import java.time.ZonedDateTime;
import java.util.Optional;

public record UserResponse(@JsonProperty(ID) String id,
                           @JsonProperty(LOGIN) String login,
                           @JsonProperty(ROLE) Role role,
                           @JsonProperty(BANNED) boolean banned,
                           @JsonProperty(CREATED_DATE) ZonedDateTime createdDate,
                           @JsonProperty(UPDATED_DATE) ZonedDateTime updatedDate) {

    private static final String ID = "id";
    private static final String LOGIN = "login";
    private static final String ROLE = "role";
    private static final String BANNED = "banned";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

}
