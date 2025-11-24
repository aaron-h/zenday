package com.zenday.service;

import com.zenday.dto.DailyPlanDTO;
import com.zenday.dto.PlanItemDTO;
import com.zenday.entity.DailyPlan;
import com.zenday.entity.PlanItem;
import com.zenday.entity.Practice;
import com.zenday.repository.DailyPlanRepository;
import com.zenday.repository.PlanItemRepository;
import com.zenday.repository.PracticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 每日计划服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DailyPlanService {

    private final DailyPlanRepository dailyPlanRepository;
    private final PlanItemRepository planItemRepository;
    private final PracticeRepository practiceRepository;

    /**
     * 创建或更新每日计划
     */
    @Transactional
    public DailyPlanDTO saveDailyPlan(Long userId, LocalDate planDate,
                                      LocalTime startTime, List<PlanItemDTO> items) {
        log.info("Saving daily plan for user: {}, date: {}", userId, planDate);

        // 查找或创建计划
        DailyPlan plan = dailyPlanRepository.findByUserIdAndPlanDate(userId, planDate)
            .orElseGet(() -> DailyPlan.builder()
                .userId(userId)
                .planDate(planDate)
                .build());

        plan.setStartTime(startTime);

        // 计算总时长和项数
        int totalDuration = items.stream().mapToInt(PlanItemDTO::getDuration).sum();
        plan.setTotalDuration(totalDuration);
        plan.setTotalCount(items.size());

        plan = dailyPlanRepository.save(plan);

        // 删除旧的计划项
        planItemRepository.deleteByPlanId(plan.getId());

        // 创建新的计划项
        LocalTime currentTime = startTime;
        List<PlanItem> planItems = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            PlanItemDTO itemDTO = items.get(i);
            Practice practice = practiceRepository.findById(itemDTO.getPracticeId())
                .orElseThrow(() -> new RuntimeException("Practice not found: " + itemDTO.getPracticeId()));

            LocalTime endTime = currentTime.plusMinutes(itemDTO.getDuration());

            PlanItem planItem = PlanItem.builder()
                .planId(plan.getId())
                .practiceId(practice.getId())
                .practiceName(practice.getName())
                .practiceIcon(practice.getIcon())
                .startTime(currentTime)
                .endTime(endTime)
                .duration(itemDTO.getDuration())
                .sortOrder(i)
                .completed(false)
                .build();

            planItems.add(planItem);
            currentTime = endTime;
        }

        planItemRepository.saveAll(planItems);

        return getDailyPlan(userId, planDate);
    }

    /**
     * 获取每日计划
     */
    public DailyPlanDTO getDailyPlan(Long userId, LocalDate planDate) {
        DailyPlan plan = dailyPlanRepository.findByUserIdAndPlanDate(userId, planDate)
            .orElse(null);

        if (plan == null) {
            return null;
        }

        List<PlanItem> items = planItemRepository.findByPlanIdOrderBySortOrderAsc(plan.getId());

        return DailyPlanDTO.builder()
            .id(plan.getId())
            .planDate(plan.getPlanDate())
            .startTime(plan.getStartTime())
            .totalDuration(plan.getTotalDuration())
            .completedDuration(plan.getCompletedDuration())
            .completedCount(plan.getCompletedCount())
            .totalCount(plan.getTotalCount())
            .completionPercentage(plan.getCompletionPercentage())
            .items(items.stream().map(this::toPlanItemDTO).collect(Collectors.toList()))
            .build();
    }

    /**
     * 批量生成未来计划（基于当天计划复制）
     */
    @Transactional
    public List<DailyPlanDTO> batchGeneratePlans(Long userId, LocalDate baseDate, int days) {
        log.info("Batch generating plans for user: {}, from: {}, days: {}", userId, baseDate, days);

        DailyPlanDTO basePlan = getDailyPlan(userId, baseDate);
        if (basePlan == null) {
            throw new RuntimeException("Base plan not found for date: " + baseDate);
        }

        List<DailyPlanDTO> generatedPlans = new ArrayList<>();

        for (int i = 1; i <= days; i++) {
            LocalDate targetDate = baseDate.plusDays(i);

            // 跳过已存在的计划
            if (dailyPlanRepository.existsByUserIdAndPlanDate(userId, targetDate)) {
                continue;
            }

            DailyPlanDTO newPlan = saveDailyPlan(userId, targetDate,
                basePlan.getStartTime(), basePlan.getItems());
            generatedPlans.add(newPlan);
        }

        return generatedPlans;
    }

    /**
     * 标记计划项完成
     */
    @Transactional
    public void markPlanItemCompleted(Long planItemId) {
        log.info("Marking plan item completed: {}", planItemId);

        PlanItem item = planItemRepository.findById(planItemId)
            .orElseThrow(() -> new RuntimeException("Plan item not found: " + planItemId));

        item.setCompleted(true);
        item.setCompletedAt(java.time.LocalDateTime.now());
        planItemRepository.save(item);

        // 更新计划的完成统计
        updatePlanStats(item.getPlanId());
    }

    /**
     * 更新计划统计
     */
    private void updatePlanStats(Long planId) {
        DailyPlan plan = dailyPlanRepository.findById(planId)
            .orElseThrow(() -> new RuntimeException("Plan not found: " + planId));

        List<PlanItem> items = planItemRepository.findByPlanIdOrderBySortOrderAsc(planId);

        long completedCount = items.stream().filter(PlanItem::getCompleted).count();
        int completedDuration = items.stream()
            .filter(PlanItem::getCompleted)
            .mapToInt(PlanItem::getDuration)
            .sum();

        plan.setCompletedCount((int) completedCount);
        plan.setCompletedDuration(completedDuration);

        dailyPlanRepository.save(plan);
    }

    private PlanItemDTO toPlanItemDTO(PlanItem item) {
        return PlanItemDTO.builder()
            .id(item.getId())
            .practiceId(item.getPracticeId())
            .practiceName(item.getPracticeName())
            .practiceIcon(item.getPracticeIcon())
            .startTime(item.getStartTime())
            .endTime(item.getEndTime())
            .duration(item.getDuration())
            .sortOrder(item.getSortOrder())
            .completed(item.getCompleted())
            .completedAt(item.getCompletedAt())
            .build();
    }
}
