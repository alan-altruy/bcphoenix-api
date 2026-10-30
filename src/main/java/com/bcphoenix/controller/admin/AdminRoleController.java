package com.bcphoenix.controller.admin;

import com.bcphoenix.dto.request.RoleRequest;
import com.bcphoenix.model.Role;
import com.bcphoenix.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
public class AdminRoleController {
    private final RoleService roleService;

    public AdminRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> findAll() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public Role findById(@PathVariable Integer id) {
        return roleService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Role create(@Valid @RequestBody RoleRequest request) {
        return roleService.create(request);
    }

    @PutMapping("/{id}")
    public Role update(@PathVariable Integer id, @Valid @RequestBody RoleRequest request){
        return roleService.update(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        roleService.delete(id);
    }
}
