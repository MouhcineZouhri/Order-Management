package com.example.ordermanagement.Repository;

import com.example.ordermanagement.Entity.Role;
import com.example.ordermanagement.AppConstants.RoleConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void test_can_create_role(){
        Role role = new Role();
        role.setName(RoleConstants.ADMIN);

        Role savedRole = roleRepository.save(role);

        Assertions.assertThat(savedRole.getId()).isNotNull();

        Assertions.assertThat(savedRole.getName()).isEqualTo(RoleConstants.ADMIN);
    }

    @Test
    public void test_get_role_by_name(){
        Role role  = new Role();

        role.setName(RoleConstants.USER);

        roleRepository.save(role);

        Role rolesByName = roleRepository.findRolesByName(RoleConstants.USER).orElse(null);

        Assertions.assertThat(rolesByName).isNotNull();

        Assertions.assertThat(rolesByName.getName()).isEqualTo(RoleConstants.USER);
    }
}