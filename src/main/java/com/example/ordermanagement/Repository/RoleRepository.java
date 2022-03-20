package com.example.ordermanagement.Repository;

import com.example.ordermanagement.Entity.AppUser;
import com.example.ordermanagement.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role , Long> {
    Optional<Role> findRolesByName(String roleName);

    List<Role> findRolesByUsers(AppUser user);
}
