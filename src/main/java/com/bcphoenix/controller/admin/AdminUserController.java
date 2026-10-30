package com.bcphoenix.controller.admin;

import com.bcphoenix.dto.request.UserRequest;
import com.bcphoenix.dto.response.admin.AdminUserResponse;
import com.bcphoenix.model.User;
import com.bcphoenix.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<AdminUserResponse> findAll() {
        List<User> users = userService.findAll();
        return users.stream().map(AdminUserResponse::from).toList();
    }

    @GetMapping("/{id}")
    public AdminUserResponse findById(@PathVariable Integer id) {
        return AdminUserResponse.from(userService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminUserResponse create(@Valid @RequestBody UserRequest request) {
        return AdminUserResponse.from(userService.create(request));
    }

    @PutMapping("/{id}")
    public AdminUserResponse update(@PathVariable Integer id, @Valid @RequestBody UserRequest request){
        return AdminUserResponse.from(userService.update(id,request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
         userService.delete(id);
    }
}
