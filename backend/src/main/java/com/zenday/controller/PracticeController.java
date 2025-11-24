package com.zenday.controller;

import com.zenday.dto.ApiResponse;
import com.zenday.entity.Practice;
import com.zenday.service.PracticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功课控制器
 */
@RestController
@RequestMapping("/practices")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class PracticeController {

    private final PracticeService practiceService;

    /**
     * 创建功课
     */
    @PostMapping
    public ApiResponse<Practice> createPractice(@RequestBody Practice practice) {
        return ApiResponse.success(practiceService.createPractice(practice));
    }

    /**
     * 更新功课
     */
    @PutMapping("/{id}")
    public ApiResponse<Practice> updatePractice(@PathVariable Long id, @RequestBody Practice practice) {
        return ApiResponse.success(practiceService.updatePractice(id, practice));
    }

    /**
     * 删除功课
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePractice(@PathVariable Long id) {
        practiceService.deletePractice(id);
        return ApiResponse.success(null);
    }

    /**
     * 获取用户功课列表
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<Practice>> getUserPractices(
        @PathVariable Long userId,
        @RequestParam(required = false) Boolean enabledOnly) {
        return ApiResponse.success(practiceService.getUserPractices(userId, enabledOnly));
    }

    /**
     * 获取功课详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Practice> getPractice(@PathVariable Long id) {
        return ApiResponse.success(practiceService.getPracticeById(id)
            .orElseThrow(() -> new RuntimeException("Practice not found: " + id)));
    }

    /**
     * 批量创建推荐功课
     */
    @PostMapping("/user/{userId}/recommended")
    public ApiResponse<List<Practice>> createRecommendedPractices(@PathVariable Long userId) {
        return ApiResponse.success(practiceService.createRecommendedPractices(userId));
    }
}
