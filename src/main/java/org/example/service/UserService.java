package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User save(User user) {
        return userDao.save(user);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public void delete(User user) {
        userDao.delete(user);
    }

    public boolean existsByEmail(String email) {
        return findAll().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
