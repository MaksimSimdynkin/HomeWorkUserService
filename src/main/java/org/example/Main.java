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
                System.out.println("\nВыберите опцию:");
                System.out.println("1. Создать пользователя");
                System.out.println("2. Показать всех пользователей");
                System.out.println("3. Найти пользователя по ID");
                System.out.println("4. Обновить пользователя");
                System.out.println("5. Удалить пользователя");
                System.out.println("6. Выход");
                System.out.print("Введите свой выбор: ");

                int choice = scanner.nextInt();
                scanner.nextLine();;

                switch (choice) {
                    case 1 -> createUser();
                    case 2 -> viewAllUsers();
                    case 3 -> viewUserById();
                    case 4 -> updateUser();
                    case 5 -> deleteUser();
                    case 6 -> running = false;
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }
        } catch (Exception e) {
            System.err.println("Произошла критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
        }
    }

    private static void createUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        System.out.print("Введите age: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(name, email, age);
        userDao.save(user);
        System.out.println("Пользователь успешно создан: " + user);
    }

    private static void viewAllUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("Пользователь не найден");
        } else {
            System.out.println("Список пользователей:");
            users.forEach(System.out::println);
        }
    }

    private static void viewUserById() {
        System.out.print("Введите id пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());

        var user = userDao.findById(id);
        if (user.isPresent()) {
            System.out.println("Пользователь найден: " + user.get());
        } else {
            System.out.println("Пользовать с: " + id + " не найден");
        }
    }

    private static void updateUser() {
        System.out.print("введите id пользователя : ");
        Long id = Long.parseLong(scanner.nextLine());

        var userOptional = userDao.findById(id);
        if (userOptional.isEmpty()) {
            System.out.println("Пользователь с id не найден: " + id);
            return;
        }

        User user = userOptional.get();

        System.out.print("Введите новое имя (текущее: " + user.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }

        System.out.print("Введите новый email (текущий: " + user.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        System.out.print("Введите новый возраст (текущий: " + user.getAge() + "): ");
        String ageInput = scanner.nextLine();
        if (!ageInput.isEmpty()) {
            user.setAge(Integer.parseInt(ageInput));
        }

        userDao.update(user);
        System.out.println("Пользователь успешно обновился: " + user);
    }

    private static void deleteUser() {
        System.out.print("Введите id пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());

        var userOptional = userDao.findById(id);
        if (userOptional.isEmpty()) {
            System.out.println("Пользователь с id не найден: " + id);
            return;
        }

        User user = userOptional.get();
        userDao.delete(user);
        System.out.println("Пользователь успешно удален: " + user);
    }
}