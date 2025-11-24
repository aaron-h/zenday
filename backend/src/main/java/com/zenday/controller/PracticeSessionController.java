package com.zenday.controller;

import com.zenday.dto.ApiResponse;
import com.zenday.entity.PracticeSession;
import com.zenday.service.PracticeSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 修行会话控制器
 */
@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class PracticeSessionController {

    private final PracticeSessionService sessionService;

    /**
     * 创建修行会话
     */
    @PostMapping
    public ApiResponse<PracticeSession> createSession(@RequestBody PracticeSession session) {
        return ApiResponse.success(sessionService.createSession(session));
    }

    /**
     * 完成修行会话
     */
    @PutMapping("/{id}/complete")
    public ApiResponse<PracticeSession> completeSession(
        @PathVariable Long id,
        @RequestBody CompleteSessionRequest request) {
        return ApiResponse.success(sessionService.completeSession(
            id, request.getEndTime(), request.getNote()));
    }

    /**
     * 获取用户的会话历史
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<PracticeSession>> getUserSessions(@PathVariable Long userId) {
        return ApiResponse.success(sessionService.getUserSessions(userId));
    }

    /**
     * 获取指定日期的会话
     */
    @GetMapping("/user/{userId}/date/{date}")
    public ApiResponse<List<PracticeSession>> getSessionsByDate(
        @PathVariable Long userId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(sessionService.getSessionsByDate(userId, date));
    }

    /**
     * 获取日期范围内的会话
     */
    @GetMapping("/user/{userId}/range")
    public ApiResponse<List<PracticeSession>> getSessionsByDateRange(
        @PathVariable Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success(sessionService.getSessionsByDateRange(userId, startDate, endDate));
    }

    @lombok.Data
    public static class CompleteSessionRequest {
        private java.time.LocalDateTime endTime;
        private String note;
    }
}
