package com.zenday.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 统计数据DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsDTO {

    /**
     * 总时长（分钟）
     */
    private Integer totalMinutes;

    /**
     * 总小时数
     */
    private Double totalHours;

    /**
     * 会话数
     */
    private Integer sessionCount;

    /**
     * 分类统计
     */
    private Map<String, CategoryStats> categoryStats;

    /**
     * 最近会话
     */
    private List<SessionSummary> recentSessions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStats {
        private String category;
        private Integer minutes;
        private Integer percentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionSummary {
        private String date;
        private String practiceName;
        private Integer duration;
    }
}
