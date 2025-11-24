package com.zenday.service;

import com.zenday.entity.Practice;
import com.zenday.repository.PracticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 功课服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PracticeService {

    private final PracticeRepository practiceRepository;

    /**
     * 创建功课
     */
    @Transactional
    public Practice createPractice(Practice practice) {
        log.info("Creating practice: {}", practice.getName());
        return practiceRepository.save(practice);
    }

    /**
     * 更新功课
     */
    @Transactional
    public Practice updatePractice(Long id, Practice practice) {
        log.info("Updating practice: {}", id);
        Practice existing = practiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Practice not found: " + id));

        existing.setName(practice.getName());
        existing.setCategory(practice.getCategory());
        existing.setDefaultDuration(practice.getDefaultDuration());
        existing.setDefaultReminderTime(practice.getDefaultReminderTime());
        existing.setDefaultSound(practice.getDefaultSound());
        existing.setDefaultVolume(practice.getDefaultVolume());
        existing.setDefaultBreathingRhythm(practice.getDefaultBreathingRhythm());
        existing.setIcon(practice.getIcon());
        existing.setDescription(practice.getDescription());
        existing.setSortOrder(practice.getSortOrder());
        existing.setEnabled(practice.getEnabled());

        return practiceRepository.save(existing);
    }

    /**
     * 删除功课
     */
    @Transactional
    public void deletePractice(Long id) {
        log.info("Deleting practice: {}", id);
        practiceRepository.deleteById(id);
    }

    /**
     * 获取用户的所有功课
     */
    public List<Practice> getUserPractices(Long userId, Boolean enabledOnly) {
        if (enabledOnly != null && enabledOnly) {
            return practiceRepository.findByUserIdAndEnabledTrueOrderBySortOrderAsc(userId);
        }
        return practiceRepository.findByUserIdOrderBySortOrderAsc(userId);
    }

    /**
     * 根据ID获取功课
     */
    public Optional<Practice> getPracticeById(Long id) {
        return practiceRepository.findById(id);
    }

    /**
     * 批量创建推荐功课
     */
    @Transactional
    public List<Practice> createRecommendedPractices(Long userId) {
        log.info("Creating recommended practices for user: {}", userId);

        List<Practice> recommendations = List.of(
            Practice.builder()
                .userId(userId)
                .name("晨起梦观")
                .category("观想类")
                .defaultDuration(30)
                .defaultReminderTime("07:00")
                .icon("🌅")
                .description("清晨醒来后，回忆并观察梦境")
                .sortOrder(1)
                .build(),
            Practice.builder()
                .userId(userId)
                .name("祈祷")
                .category("祈祷类")
                .defaultDuration(10)
                .defaultReminderTime("07:30")
                .icon("🙏")
                .description("虔诚祈祷，连接内在神性")
                .sortOrder(2)
                .build(),
            Practice.builder()
                .userId(userId)
                .name("瑜伽")
                .category("运动类")
                .defaultDuration(30)
                .defaultReminderTime("08:00")
                .icon("🧘")
                .description("哈他瑜伽体式练习")
                .sortOrder(3)
                .build(),
            Practice.builder()
                .userId(userId)
                .name("打坐")
                .category("打坐类")
                .defaultDuration(30)
                .defaultReminderTime("19:00")
                .icon("🪷")
                .description("静坐冥想，观照内心")
                .sortOrder(4)
                .build(),
            Practice.builder()
                .userId(userId)
                .name("诵读心经")
                .category("诵读类")
                .defaultDuration(20)
                .defaultReminderTime("09:00")
                .icon("📿")
                .description("诵读《般若波罗蜜多心经》")
                .sortOrder(5)
                .build(),
            Practice.builder()
                .userId(userId)
                .name("无常观")
                .category("观想类")
                .defaultDuration(20)
                .defaultReminderTime("20:00")
                .icon("🍂")
                .description("观察万物生灭，体悟无常")
                .sortOrder(6)
                .build(),
            Practice.builder()
                .userId(userId)
                .name("空性观")
                .category("观想类")
                .defaultDuration(25)
                .defaultReminderTime("20:30")
                .icon("⭕")
                .description("观照空性，了悟缘起")
                .sortOrder(7)
                .build(),
            Practice.builder()
                .userId(userId)
                .name("睡前祈祷")
                .category("祈祷类")
                .defaultDuration(10)
                .defaultReminderTime("22:00")
                .icon("🌙")
                .description("睡前感恩祈祷")
                .sortOrder(8)
                .build()
        );

        return practiceRepository.saveAll(recommendations);
    }
}
