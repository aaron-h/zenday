package com.zenday.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 功课项目实体
 */
@Entity
@Table(name = "practices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Practice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属用户ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 功课名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 功课分类（观想类、诵读类、打坐类等）
     */
    @Column(length = 50)
    private String category;

    /**
     * 默认时长（分钟）
     */
    @Column(nullable = false)
    private Integer defaultDuration = 30;

    /**
     * 默认提醒时间（HH:mm格式）
     */
    @Column(length = 10)
    private String defaultReminderTime;

    /**
     * 默认禅音（磬声、木鱼、钟声、风铃、古琴）
     */
    @Column(length = 50)
    private String defaultSound = "qing";

    /**
     * 默认音量（0-100）
     */
    @Column
    private Integer defaultVolume = 50;

    /**
     * 默认呼吸节奏（slow、medium、fast）
     */
    @Column(length = 20)
    private String defaultBreathingRhythm = "medium";

    /**
     * 图标/emoji
     */
    @Column(length = 50)
    private String icon = "🧘";

    /**
     * 描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 排序序号
     */
    @Column
    private Integer sortOrder = 0;

    /**
     * 是否启用
     */
    @Column(nullable = false)
    private Boolean enabled = true;

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
