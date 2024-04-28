package com.flacko.user.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.user.service.UserRole;

import java.time.ZonedDateTime;

public record UserResponse(@JsonProperty(ID) String id,
                           @JsonProperty(LOGIN) String login,
                           @JsonProperty(ROLE) UserRole role,
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
