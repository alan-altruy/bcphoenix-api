package com.bcphoenix.service;

import com.bcphoenix.dto.request.RoleRequest;
import com.bcphoenix.model.Role;
import com.bcphoenix.repository.RoleRepository;
import com.bcphoenix.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class RoleService {

    private static final String ADMIN_ROLE = "ADMIN";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role create(RoleRequest request) {
        String name = request.name().trim().toUpperCase(Locale.ROOT);

        if (roleRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Le rôle '" + name + "' existe déjà");
        }

        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }

    public Role update(Integer id, RoleRequest request) {
        Role role = findById(id);
        String name = request.name().trim().toUpperCase(Locale.ROOT);
        if (roleRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Le rôle '" + name + "' existe déjà");
        }
        if (ADMIN_ROLE.equals(role.getName()) && !ADMIN_ROLE.equals(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Le rôle ADMIN ne peut pas être renommé");
        }
        role.setName(name);
        return roleRepository.save(role);

    }

    public Role findById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role introuvable"));
    }

    public void delete(Integer id) {
        Role role = findById(id);                       // 404 s'il n'existe pas

        if (userRepository.existsByRoleId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Le rôle '" + role.getName() + "' est attribué à des utilisateurs");
        }

        roleRepository.delete(role);
    }
}
