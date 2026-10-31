package com.bcphoenix.service;

import com.bcphoenix.dto.request.PasswordChangeRequest;
import com.bcphoenix.dto.request.UserCreateRequest;
import com.bcphoenix.dto.request.UserUpdateRequest;
import com.bcphoenix.model.Role;
import com.bcphoenix.model.User;
import com.bcphoenix.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(UserCreateRequest request) {

        if(userRepository.existsByEmailIgnoreCase(request.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"L'adresse email: "+request.email()+" est déjà utilisée");
        }
        User user = new User();
        Role role = roleService.findById(request.roleId());

        user.setLastName(request.lastName());
        user.setFirstName(request.firstName());
        user.setRole(role);
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        return userRepository.save(user);
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
    }
    public User update(Integer id, UserUpdateRequest request){
        User user=findById(id);
        if(userRepository.existsByEmailIgnoreCaseAndIdNot(request.email(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"L'adresse email: "+request.email()+" est déjà utilisée");
        }
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
    public User findByEmail(String email){
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Compte introuvable"));
    }
    public void changePassword(String email, PasswordChangeRequest request){
        User user = findByEmail(email);
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mot de passe actuel incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
