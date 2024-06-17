package com.iablonski.crm.tasklist.service;

import com.iablonski.crm.tasklist.domain.exception.ResourceMappingException;
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

    @Test
    void getById_UserNotFound() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userServiceImpl.getById(1L))
                .isInstanceOf(ResourceMappingException.class)
                .hasMessage("User not found");

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(1L);
    }

    @Test
    void getByUsername_UserExists() {
        Mockito.when(userRepository.findByUsername("john_doe"))
                .thenReturn(Optional.of(user));

        User foundedUser = userServiceImpl.getByUsername("john_doe");

        Assertions.assertThat(foundedUser).isNotNull();
        Assertions.assertThat(foundedUser.getUsername())
                .isEqualTo("john_doe");
        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername("john_doe");
    }

    @Test
    void getByUsername_UserNotFound() {
        Mockito.when(userRepository.findByUsername("john_doe"))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userServiceImpl
                        .getByUsername("john_doe"))
                .isInstanceOf(ResourceMappingException.class)
                .hasMessage("User not found");

        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername("john_doe");
    }

    @Test
    void update_userUpdated() {
        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("encodedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        User updatedUser = userServiceImpl.update(user);

        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getPassword())
                .isEqualTo("encodedPassword");
        Mockito.verify(passwordEncoder, Mockito.times(1))
                .encode("password");
        Mockito.verify(userRepository, Mockito.times(1))
                .save(user);
    }

    @Test
    void create_userCreated() {
        Mockito.when(userRepository.findByUsername("john_doe"))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("encodedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        User createdUser = userServiceImpl.create(user);

        System.out.println(user.getPassword());

        Assertions.assertThat(createdUser).isNotNull();
        Assertions.assertThat(createdUser.getPassword())
                .isEqualTo("encodedPassword");
        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername("john_doe");
        Mockito.verify(passwordEncoder, Mockito.times(1))
                .encode("password");
        Mockito.verify(userRepository, Mockito.times(1))
                .save(user);
    }

    @Test
    void create_userAlreadyExists() {
        Mockito.when(userRepository.findByUsername("john_doe"))
                .thenReturn(Optional.of(user));

        Assertions.assertThatThrownBy(() -> userServiceImpl
                        .create(user))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User already exists");

        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername("john_doe");
        Mockito.verify(userRepository, Mockito.times(0))
                .save(user);
    }

    @Test
    void isTaskOwner_isTaskOwner() {
        Mockito.when(userRepository.isTaskOwner(1L, 1L))
                .thenReturn(true);

        boolean isOwner = userServiceImpl.isTaskOwner(1L, 1L);

        Assertions.assertThat(isOwner).isTrue();
        Mockito.verify(userRepository, Mockito.times(1))
                .isTaskOwner(1L, 1L);
    }

    @Test
    void isTaskOwner_isNotTaskOwner() {
        Mockito.when(userRepository.isTaskOwner(1L, 1L))
                .thenReturn(false);

        boolean isOwner = userServiceImpl.isTaskOwner(1L, 1L);

        Assertions.assertThat(isOwner).isFalse();
        Mockito.verify(userRepository, Mockito.times(1))
                .isTaskOwner(1L, 1L);
    }

    @Test
    void delete_userDeleted() {
        Mockito.doNothing().when(userRepository).deleteById(1L);

        userServiceImpl.delete(1L);

        Mockito.verify(userRepository, Mockito.times(1))
                .deleteById(1L);
    }
}
