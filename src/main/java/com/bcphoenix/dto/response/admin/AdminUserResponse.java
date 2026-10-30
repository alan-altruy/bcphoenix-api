package com.bcphoenix.dto.response.admin;


import com.bcphoenix.model.User;

public record AdminUserResponse(
        Integer id,
        String lastName,
        String firstName,
        String roleName,
        String email
) {
    public static AdminUserResponse from(User user){
       return new AdminUserResponse(
               user.getId(),
               user.getLastName(),
               user.getFirstName(),
               user.getRole().getName(),
               user.getEmail()
       ) ;
    }
}
