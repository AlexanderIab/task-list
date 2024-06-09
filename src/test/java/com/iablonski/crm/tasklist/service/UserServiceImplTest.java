package com.iablonski.crm.tasklist.service;

import com.iablonski.crm.tasklist.domain.user.User;
import com.iablonski.crm.tasklist.repository.UserRepository;
import com.iablonski.crm.tasklist.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userServiceImpl;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setPassword("password");
        user.setPasswordConfirmation("password");
    }


    @Test
    void getById_UserExists() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundedUser = userServiceImpl.getById(1L);

        Assertions.assertThat(foundedUser).isNotNull();
        Assertions.assertThat(foundedUser.getUsername())
                .isEqualTo("john_doe");
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(1L);
    }


}
