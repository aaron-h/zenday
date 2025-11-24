package com.zenday.controller;

import com.zenday.dto.ApiResponse;
import com.zenday.dto.DailyPlanDTO;
import com.zenday.service.DailyPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 每日计划控制器
 */
@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class DailyPlanController {

    private final DailyPlanService dailyPlanService;

    /**
     * 保存每日计划
     */
    @PostMapping
    public ApiResponse<DailyPlanDTO> saveDailyPlan(@RequestBody DailyPlanDTO planDTO) {
        return ApiResponse.success(dailyPlanService.saveDailyPlan(
            1L, // TODO: 从session获取当前用户ID
            planDTO.getPlanDate(),
            planDTO.getStartTime(),
            planDTO.getItems()
        ));
    }

    /**
     * 获取每日计划
     */
    @GetMapping("/user/{userId}/date/{date}")
    public ApiResponse<DailyPlanDTO> getDailyPlan(
        @PathVariable Long userId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(dailyPlanService.getDailyPlan(userId, date));
    }

    /**
     * 批量生成未来计划
     */
    @PostMapping("/user/{userId}/batch")
    public ApiResponse<List<DailyPlanDTO>> batchGeneratePlans(
        @PathVariable Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate,
        @RequestParam Integer days) {
        return ApiResponse.success(dailyPlanService.batchGeneratePlans(userId, baseDate, days));
    }

    /**
     * 标记计划项完成
     */
    @PutMapping("/items/{itemId}/complete")
    public ApiResponse<Void> markPlanItemCompleted(@PathVariable Long itemId) {
        dailyPlanService.markPlanItemCompleted(itemId);
        return ApiResponse.success(null);
    }
}
