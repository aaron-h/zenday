package com.zenday.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 计划项实体
 */
@Entity
@Table(name = "plan_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属计划ID
     */
    @Column(nullable = false)
    private Long planId;

    /**
     * 功课ID
     */
    @Column(nullable = false)
    private Long practiceId;

    /**
     * 功课名称（冗余，便于查询）
     */
    @Column(nullable = false, length = 100)
    private String practiceName;

    /**
     * 功课图标
     */
    @Column(length = 50)
    private String practiceIcon;

    /**
     * 开始时间
     */
    @Column(nullable = false)
    private LocalTime startTime;

    /**
     * 结束时间
     */
    @Column(nullable = false)
    private LocalTime endTime;

    /**
     * 时长（分钟）
     */
    @Column(nullable = false)
    private Integer duration;

    /**
     * 排序序号
     */
    @Column(nullable = false)
    private Integer sortOrder = 0;

    /**
     * 是否已完成
     */
    @Column(nullable = false)
    private Boolean completed = false;

    /**
     * 完成时间
     */
    @Column
    private LocalDateTime completedAt;

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
}
