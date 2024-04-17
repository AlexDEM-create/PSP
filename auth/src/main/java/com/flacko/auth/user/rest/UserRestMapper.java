package com.flacko.auth.user.rest;

import com.flacko.auth.user.User;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class UserRestMapper {

    UserResponse mapModelToResponse(User user) {
        // add timezone from authorization
        return new UserResponse(user.getId(),
                user.getLogin(),
                user.getRole(),
                user.isBanned(),
                user.getCreatedDate().atZone(ZoneId.systemDefault()),
                user.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
