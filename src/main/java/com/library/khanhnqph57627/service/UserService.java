package com.library.khanhnqph57627.service;

import com.library.khanhnqph57627.entity.User;
import com.library.khanhnqph57627.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void initSampleData() {
        if (userRepository.count() == 0) {
            User user1 = new User();
            user1.setUsername("student1");
            user1.setPassword("password123");
            user1.setFullName("Nguyễn Văn A");
            user1.setEmail("student1@university.edu.vn");
            user1.setRole("STUDENT");

            User user2 = new User();
            user2.setUsername("teacher1");
            user2.setPassword("password123");
            user2.setFullName("PGS.TS. Trần Thị B");
            user2.setEmail("teacher1@university.edu.vn");
            user2.setRole("TEACHER");

            User user3 = new User();
            user3.setUsername("admin");
            user3.setPassword("admin123");
            user3.setFullName("Quản trị viên Hệ thống");
            user3.setEmail("admin@university.edu.vn");
            user3.setRole("ADMIN");

            User user4 = new User();
            user4.setUsername("student2");
            user4.setPassword("password123");
            user4.setFullName("Lê Văn C");
            user4.setEmail("student2@university.edu.vn");
            user4.setRole("STUDENT");

            userRepository.saveAll(Arrays.asList(user1, user2, user3, user4));
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}