package com.zenday.service;

import com.zenday.entity.User;
import com.zenday.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * 创建用户
     */
    @Transactional
    public User createUser(User user) {
        log.info("Creating user: {}", user.getUsername());

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }

        return userRepository.save(user);
    }

    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 根据ID获取用户
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 根据用户名获取用户
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 更新用户
     */
    @Transactional
    public User updateUser(Long id, User user) {
        log.info("Updating user: {}", id);

        User existing = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found: " + id));

        existing.setNickname(user.getNickname());
        existing.setAvatar(user.getAvatar());

        return userRepository.save(existing);
    }

    /**
     * 删除用户
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user: {}", id);
        userRepository.deleteById(id);
    }

    /**
     * 获取或创建默认用户
     */
    @Transactional
    public User getOrCreateDefaultUser() {
        return userRepository.findByUsername("default")
            .orElseGet(() -> {
                User defaultUser = User.builder()
                    .username("default")
                    .nickname("修行者")
                    .isAdmin(false)
                    .totalMinutes(0)
                    .build();
                return userRepository.save(defaultUser);
            });
    }
}
