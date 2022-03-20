package com.example.ordermanagement.Repository;

import com.example.ordermanagement.Entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser , Long> {
    Optional<AppUser> findAppUserByUsername(String username);

    void deleteAppUserByUsername(String username);

    @Override
    Page<AppUser> findAll(Pageable pageable);
}
