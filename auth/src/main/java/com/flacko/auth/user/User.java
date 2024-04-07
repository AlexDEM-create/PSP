package com.flacko.auth.user;

public interface User {

    String getId();

    String getLogin();

    String getPassword();

    Role getRole();

}
