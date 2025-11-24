package com.zenday.controller;

import com.zenday.dto.ApiResponse;
import com.zenday.dto.StatsDTO;
import com.zenday.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 统计控制器
 */
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class StatsController {

    private final StatsService statsService;

    /**
     * 获取统计数据
     * @param range today, week, month, all
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<StatsDTO> getStats(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "week") String range) {
        return ApiResponse.success(statsService.getStats(userId, range));
    }
}
