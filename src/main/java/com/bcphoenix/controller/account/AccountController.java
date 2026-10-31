package com.bcphoenix.controller.account;

import com.bcphoenix.dto.request.PasswordChangeRequest;
import com.bcphoenix.dto.response.AccountResponse;
import com.bcphoenix.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final UserService userService;

    public AccountController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/me")
    public AccountResponse me(Authentication authentication){
        return AccountResponse.from(userService.findByEmail(authentication.getName()));
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(Authentication authentication, @Valid @RequestBody PasswordChangeRequest request){
        userService.changePassword(authentication.getName(), request);
    }
}
