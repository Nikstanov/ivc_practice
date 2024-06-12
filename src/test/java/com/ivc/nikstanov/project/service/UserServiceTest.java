package com.ivc.nikstanov.project.service;

import com.ivc.nikstanov.project.dto.UserDto;
import com.ivc.nikstanov.project.entity.User;
import com.ivc.nikstanov.project.exception.EmailAlreadyExistsException;
import com.ivc.nikstanov.project.exception.ResourceNotFoundException;
import com.ivc.nikstanov.project.mapper.AutoUserMapper;
import com.ivc.nikstanov.project.repository.UserRepository;
import com.ivc.nikstanov.project.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AutoUserMapper userMapper;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void createUser() {
        var newUser = new UserDto(
                0L,
                "test",
                "test",
                "mail"
        );
        var savedUser = new User(
                newUser.getId(),
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getEmail()
        );

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userMapper.mapToUser(newUser)).thenReturn(savedUser);
        when(userRepository.save(savedUser)).thenReturn(savedUser);
        when(userMapper.mapToUserDto(savedUser)).thenReturn(newUser);
        var result = userService.createUser(newUser);
        assertEquals(newUser, result);
        Mockito.verify(userRepository).findByEmail(newUser.getEmail());
        Mockito.verify(userMapper).mapToUser(newUser);
        Mockito.verify(userRepository).save(savedUser);
        Mockito.verify(userMapper).mapToUserDto(savedUser);
    }

    @Test
    void createUserWhenAlreadyExists() {
        var newUser = new UserDto(
                0L,
                "test",
                "test",
                "mail"
        );
        var savedUser = new User(
                newUser.getId(),
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getEmail()
        );

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(savedUser));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(newUser));
        Mockito.verify(userRepository).findByEmail(newUser.getEmail());
    }

    @Test
    void getUserById() {
        var testUser = new User(
                10L,
                "test",
                "test",
                "mail"
        );
        var expected = new UserDto(
                testUser.getId(),
                testUser.getFirstName(),
                testUser.getLastName(),
                testUser.getEmail()
        );

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userMapper.mapToUserDto(testUser)).thenReturn(expected);

        var result = userService.getUserById(testUser.getId());

        Mockito.verify(userRepository).findById(testUser.getId());
        Mockito.verify(userMapper).mapToUserDto(testUser);
        assertNotNull(result);

        assertEquals(expected, result);
    }

    @Test
    void getUserByIdWhenNotFound() {
        long testInd = 10L;
        when(userRepository.findById(testInd)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(testInd));
        Mockito.verify(userRepository).findById(testInd);
    }

    @Test
    void getAllUsers() {
        var testUser = new User(
                1L,
                "test",
                "test",
                "mail"
        );
        var expected = new UserDto(
                testUser.getId(),
                testUser.getFirstName(),
                testUser.getLastName(),
                testUser.getEmail()
        );

        when(userRepository.findAll()).thenReturn(List.of(testUser));
        when(userMapper.mapToUserDto(testUser)).thenReturn(expected);

        var result = userService.getAllUsers();

        Mockito.verify(userRepository).findAll();
        Mockito.verify(userMapper).mapToUserDto(testUser);
        assertNotNull(result);

        assertEquals(expected, result.get(0));
    }

    @Test
    void updateUser() {
        var testUser = new User(
                1L,
                "test",
                "test",
                "mail"
        );
        var newUser = new User(
                testUser.getId(),
                "new",
                "new",
                "new"
        );
        var expected = new UserDto(
                testUser.getId(),
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getEmail()
        );
        when(userRepository.findById(expected.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any())).thenReturn(newUser);
        when(userMapper.mapToUserDto(newUser)).thenReturn(expected);
        var result = userService.updateUser(expected);

        Mockito.verify(userRepository).findById(testUser.getId());
        Mockito.verify(userRepository).save(any());
        Mockito.verify(userMapper).mapToUserDto(newUser);
        assertEquals(expected, result);
    }

    @Test
    void updateUserWhenNoExists() {
        long testInd = 10L;
        when(userRepository.findById(testInd)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(new UserDto(10L, "", "", "")));
        Mockito.verify(userRepository).findById(testInd);
    }

    @Test
    void deleteUser() {
        long testInd = 10L;
        when(userRepository.findById(testInd)).thenReturn(Optional.of(new User()));
        userService.deleteUser(testInd);
        Mockito.verify(userRepository).findById(testInd);
        Mockito.verify(userRepository).deleteById(testInd);
    }

    @Test
    void deleteUserWhenNotExists() {
        long testInd = 10L;
        when(userRepository.findById(testInd)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(testInd));
        Mockito.verify(userRepository).findById(testInd);
    }
}