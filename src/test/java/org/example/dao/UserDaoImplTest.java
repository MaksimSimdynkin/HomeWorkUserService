package org.example.dao;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static UserDaoImpl userDao;
    private static User testUser;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());


        userDao = new UserDaoImpl();
        testUser = new User("Test User", "test@example.com", 30);
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.shutdown();
    }

    @Test
    @Order(1)
    void save_ShouldPersistUser() {
        User savedUser = userDao.save(testUser);

        assertNotNull(savedUser.getId());
        assertEquals(testUser.getName(), savedUser.getName());
        assertEquals(testUser.getEmail(), savedUser.getEmail());
        assertEquals(testUser.getAge(), savedUser.getAge());
        assertNotNull(savedUser.getCreatedAt());
    }

    @Test
    @Order(2)
    void findById_ShouldReturnSavedUser() {
        Optional<User> foundUser = userDao.findById(testUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getId(), foundUser.get().getId());
        assertEquals(testUser.getName(), foundUser.get().getName());
    }

    @Test
    @Order(3)
    void findAll_ShouldReturnAllUsers() {
        User anotherUser = new User("Another User", "another@example.com", 25);
        userDao.save(anotherUser);

        List<User> users = userDao.findAll();

        assertFalse(users.isEmpty());
        assertTrue(users.size() >= 1);
    }

    @Test
    @Order(4)
    void update_ShouldModifyExistingUser() {
        testUser.setName("Updated Name");
        testUser.setAge(35);

        userDao.update(testUser);

        Optional<User> updatedUser = userDao.findById(testUser.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals("Updated Name", updatedUser.get().getName());
        assertEquals(35, updatedUser.get().getAge());
    }

    @Test
    @Order(5)
    void delete_ShouldRemoveUser() {
        userDao.delete(testUser);

        Optional<User> deletedUser = userDao.findById(testUser.getId());
        assertFalse(deletedUser.isPresent());
    }
  
}