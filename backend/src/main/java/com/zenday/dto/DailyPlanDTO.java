package com.zenday.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 每日计划DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlanDTO {

    private Long id;
    private LocalDate planDate;
    private LocalTime startTime;
    private Integer totalDuration;
    private Integer completedDuration;
    private Integer completedCount;
    private Integer totalCount;
    private Double completionPercentage;
    private List<PlanItemDTO> items;
}
