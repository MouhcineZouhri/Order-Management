package com.example.ordermanagement.Repository;

import com.example.ordermanagement.Factory.UserFactory;
import com.example.ordermanagement.Entity.AppUser;
import com.example.ordermanagement.Entity.Role;
import com.example.ordermanagement.AppConstants.RoleConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.assertj.core.api.Assertions;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;


@DataJpaTest
@Import(UserFactory.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private RoleRepository  roleRepository;

    @Test
    public void can_create_user(){
        AppUser user = userFactory.createUser();

        AppUser dbUser = appUserRepository.save(user);

        Assertions.assertThat(dbUser.getId()).isNotNull();
    }

    @Test
    public void add_role_to_user(){
        Role role = new Role();
        role.setName(RoleConstants.ADMIN);
        Role dbRole = roleRepository.save(role);

        AppUser user = userFactory.createUser();
        AppUser dbUser = appUserRepository.save(user);

        dbUser.getRoles().add(dbRole);

        AppUser userWithRole = appUserRepository.save(dbUser);

        Assertions.assertThat(userWithRole.getRoles()).contains(role);
    }
}
