package com.zenday.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 修行会话（历史记录）实体
 */
@Entity
@Table(name = "practice_sessions", indexes = {
    @Index(name = "idx_user_date", columnList = "user_id, practice_date"),
    @Index(name = "idx_practice", columnList = "practice_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PracticeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属用户ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 功课ID
     */
    @Column(nullable = false)
    private Long practiceId;

    /**
     * 功课名称（冗余）
     */
    @Column(nullable = false, length = 100)
    private String practiceName;

    /**
     * 功课分类（冗余）
     */
    @Column(length = 50)
    private String practiceCategory;

    /**
     * 修行日期
     */
    @Column(nullable = false)
    private LocalDate practiceDate;

    /**
     * 开始时间
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column
    private LocalDateTime endTime;

    /**
     * 时长（分钟）
     */
    @Column(nullable = false)
    private Integer duration;

    /**
     * 实际时长（分钟，暂停会影响）
     */
    @Column
    private Integer actualDuration;

    /**
     * 禅音
     */
    @Column(length = 50)
    private String sound;

    /**
     * 音量
     */
    @Column
    private Integer volume;

    /**
     * 呼吸节奏
     */
    @Column(length = 20)
    private String breathingRhythm;

    /**
     * 备注
     */
    @Column(length = 500)
    private String note;

    /**
     * 所属计划项ID（如果是从计划执行）
     */
    @Column
    private Long planItemId;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
