package org.example;

import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.entity.User;
import org.example.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final UserDao userDao = new UserDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            boolean running = true;
            while (running) {
                System.out.println("\nUser Management System");
                System.out.println("1. Create User");
                System.out.println("2. View All Users");
                System.out.println("3. View User by ID");
                System.out.println("4. Update User");
                System.out.println("5. Delete User");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> createUser();
                    case 2 -> viewAllUsers();
                    case 3 -> viewUserById();
                    case 4 -> updateUser();
                    case 5 -> deleteUser();
                    case 6 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
        }
    }

    private static void createUser() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(name, email, age);
        userDao.save(user);
        System.out.println("User created successfully: " + user);
    }

    private static void viewAllUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("List of Users:");
            users.forEach(System.out::println);
        }
    }

    private static void viewUserById() {
        System.out.print("Enter user ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        var user = userDao.findById(id);
        if (user.isPresent()) {
            System.out.println("User found: " + user.get());
        } else {
            System.out.println("User not found with ID: " + id);
        }
    }

    private static void updateUser() {
        System.out.print("Enter user ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());

        var userOptional = userDao.findById(id);
        if (userOptional.isEmpty()) {
            System.out.println("User not found with ID: " + id);
            return;
        }

        User user = userOptional.get();

        System.out.print("Enter new name (current: " + user.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }

        System.out.print("Enter new email (current: " + user.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        System.out.print("Enter new age (current: " + user.getAge() + "): ");
        String ageInput = scanner.nextLine();
        if (!ageInput.isEmpty()) {
            user.setAge(Integer.parseInt(ageInput));
        }

        userDao.update(user);
        System.out.println("User updated successfully: " + user);
    }

    private static void deleteUser() {
        System.out.print("Enter user ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());

        var userOptional = userDao.findById(id);
        if (userOptional.isEmpty()) {
            System.out.println("User not found with ID: " + id);
            return;
        }

        User user = userOptional.get();
        userDao.delete(user);
        System.out.println("User deleted successfully: " + user);
    }
}