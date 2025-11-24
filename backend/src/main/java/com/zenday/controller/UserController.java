package com.zenday.controller;

import com.zenday.dto.ApiResponse;
import com.zenday.entity.User;
import com.zenday.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class UserController {

    private final UserService userService;

    /**
     * 创建用户
     */
    @PostMapping
    public ApiResponse<User> createUser(@RequestBody User user) {
        return ApiResponse.success(userService.createUser(user));
    }

    /**
     * 获取所有用户
     */
    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        return ApiResponse.success(userService.getAllUsers());
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public ApiResponse<User> getUser(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserById(id)
            .orElseThrow(() -> new RuntimeException("User not found: " + id)));
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ApiResponse.success(userService.updateUser(id, user));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success(null);
    }

    /**
     * 获取或创建默认用户
     */
    @GetMapping("/default")
    public ApiResponse<User> getOrCreateDefaultUser() {
        return ApiResponse.success(userService.getOrCreateDefaultUser());
    }
}
