package com.zenday.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 每日计划实体
 */
@Entity
@Table(name = "daily_plans", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "plan_date"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属用户ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 计划日期
     */
    @Column(nullable = false)
    private LocalDate planDate;

    /**
     * 起始时间
     */
    @Column(nullable = false)
    private LocalTime startTime;

    /**
     * 总时长（分钟）
     */
    @Column(nullable = false)
    private Integer totalDuration = 0;

    /**
     * 完成时长（分钟）
     */
    @Column(nullable = false)
    private Integer completedDuration = 0;

    /**
     * 完成项数
     */
    @Column(nullable = false)
    private Integer completedCount = 0;

    /**
     * 总项数
     */
    @Column(nullable = false)
    private Integer totalCount = 0;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 计算完成百分比
     */
    @Transient
    public double getCompletionPercentage() {
        if (totalCount == 0) return 0.0;
        return (double) completedCount / totalCount * 100;
    }
}
