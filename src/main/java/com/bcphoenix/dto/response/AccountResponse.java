package com.bcphoenix.dto.response;

import com.bcphoenix.model.User;

public record AccountResponse(
        Integer id,
        String lastName,
        String firstName,
        String email,
        String role) {

    public static AccountResponse from(User user){

        return new AccountResponse(user.getId(), user.getLastName(), user.getFirstName(), user.getEmail(), user.getRole().getName());
    }
}
