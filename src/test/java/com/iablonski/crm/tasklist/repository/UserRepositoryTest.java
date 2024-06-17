package com.iablonski.crm.tasklist.repository;

import com.iablonski.crm.tasklist.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles(profiles = "test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        entityManager.persistAndFlush(user);
    }

    @Test
    void findByUsernameUserExists() {
        String username = "user";

        Optional<User> foundUser = userRepository.findByUsername(username);

        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get().getUsername())
                .isEqualTo("user");
    }
}
