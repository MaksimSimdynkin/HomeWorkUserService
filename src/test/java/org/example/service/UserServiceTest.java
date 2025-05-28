package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Test User", "test@example.com", 30);
        testUser.setId(1L);
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void findById_WhenUserExists_ReturnsUser() {
        when(userDao.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userDao, times(1)).findById(1L);
    }

    @Test
    void findById_WhenUserNotExists_ReturnsEmpty() {
        when(userDao.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(99L);

        assertTrue(result.isEmpty());
        verify(userDao, times(1)).findById(99L);
    }

    @Test
    void findAll_ReturnsAllUsers() {
        List<User> users = List.of(testUser);
        when(userDao.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userDao, times(1)).findAll();
    }

    @Test
    void save_ValidUser_ReturnsSavedUser() {
        when(userDao.save(any(User.class))).thenReturn(testUser);

        User newUser = new User("New User", "new@example.com", 25);
        User result = userService.save(newUser);

        assertNotNull(result.getId());
        assertEquals(testUser, result);
        verify(userDao, times(1)).save(newUser);
    }

    @Test
    void update_ExistingUser_UpdatesUser() {
        User updatedUser = new User("Updated User", "updated@example.com", 35);
        updatedUser.setId(1L);

        doNothing().when(userDao).update(updatedUser);

        userService.update(updatedUser);

        verify(userDao, times(1)).update(updatedUser);
    }

    @Test
    void delete_ExistingUser_DeletesUser() {
        doNothing().when(userDao).delete(testUser);

        userService.delete(testUser);

        verify(userDao, times(1)).delete(testUser);
    }

    @Test
    void existsByEmail_WhenEmailExists_ReturnsTrue() {
        when(userDao.findAll()).thenReturn(List.of(testUser));

        boolean result = userService.existsByEmail("test@example.com");

        assertTrue(result);
    }

    @Test
    void existsByEmail_WhenEmailNotExists_ReturnsFalse() {
        when(userDao.findAll()).thenReturn(List.of(testUser));

        boolean result = userService.existsByEmail("nonexistent@example.com");

        assertFalse(result);
    }
}