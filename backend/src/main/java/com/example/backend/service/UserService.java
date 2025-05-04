package com.example.backend.service;

import com.example.backend.models.User;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String uid) {
        return userRepository.findById(uid);
    }

    public User createUser(User user) {
        if (user.getUid() == null || user.getUid().isEmpty()) {
            throw new IllegalArgumentException("UID không được để trống.");
        }
        return userRepository.save(user);
    }

    public User updateUser(String uid, User updatedUser) {
        return userRepository.findById(uid)
                .map(user -> {
                    user.setDisplayName(updatedUser.getDisplayName());
                    user.setPhone(updatedUser.getPhone());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

    public void deleteUser(String uid) {
        if (!userRepository.existsById(uid)) {
            throw new RuntimeException("User không tồn tại");
        }
        userRepository.deleteById(uid);
    }
}
