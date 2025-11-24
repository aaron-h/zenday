package com.zenday.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 计划项DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanItemDTO {

    private Long id;
    private Long practiceId;
    private String practiceName;
    private String practiceIcon;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer duration;
    private Integer sortOrder;
    private Boolean completed;
    private LocalDateTime completedAt;
}
