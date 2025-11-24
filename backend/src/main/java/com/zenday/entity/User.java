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
 * 用户实体
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 昵称
     */
    @Column(length = 100)
    private String nickname;

    /**
     * 头像URL
     */
    @Column(length = 500)
    private String avatar;

    /**
     * 是否是管理员
     */
    @Column(nullable = false)
    private Boolean isAdmin = false;

    /**
     * 累计修行时长（分钟）
     */
    @Column(nullable = false)
    private Integer totalMinutes = 0;

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
