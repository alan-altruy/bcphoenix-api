package com.bcphoenix.service;

import com.bcphoenix.dto.request.UserRequest;
import com.bcphoenix.model.Role;
import com.bcphoenix.model.User;
import com.bcphoenix.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(UserRequest request) {
        User user = new User();
        Role role = roleService.findById(request.roleId());

        user.setLastName(request.lastName());
        user.setFirstName(request.firstName());
        user.setRole(role);
        user.setEmail(request.email());
        return userRepository.save(user);
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
    }
    public User update(Integer id, UserRequest request){
        User user=findById(id);
        Role role = roleService.findById(request.roleId());
        user.setRole(role);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        return userRepository.save(user);
    }
    public void delete(Integer id){
        User user = findById(id);
        userRepository.delete(user);
    }
}
