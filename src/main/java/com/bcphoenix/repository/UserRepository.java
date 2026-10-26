package com.bcphoenix.repository;

import com.bcphoenix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByRoleId(Integer roleId);
}
