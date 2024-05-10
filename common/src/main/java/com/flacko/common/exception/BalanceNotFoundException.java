package com.flacko.common.exception;

import com.flacko.common.role.UserRole;

public class BalanceNotFoundException extends NotFoundException {

    public BalanceNotFoundException(String id) {
        super(String.format("Balance %s not found", id));
    }

    public BalanceNotFoundException(UserRole userRole, String userId) {
        super(String.format("Balance not found for %s user %s", userRole, userId));
    }

}
