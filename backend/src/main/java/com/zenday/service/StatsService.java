package com.zenday.service;

import com.zenday.dto.StatsDTO;
import com.zenday.entity.PracticeSession;
import com.zenday.repository.PracticeSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final PracticeSessionRepository sessionRepository;

    /**
     * 获取统计数据
     */
    public StatsDTO getStats(Long userId, String range) {
        log.info("Getting stats for user: {}, range: {}", userId, range);

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calculateStartDate(endDate, range);

        // 获取时间范围内的会话
        List<PracticeSession> sessions = sessionRepository
            .findByUserIdAndPracticeDateBetweenOrderByStartTimeDesc(userId, startDate, endDate);

        // 计算总时长
        int totalMinutes = sessions.stream()
            .mapToInt(PracticeSession::getDuration)
            .sum();

        // 按分类统计
        Map<String, Integer> categoryMinutes = sessions.stream()
            .collect(Collectors.groupingBy(
                s -> s.getPracticeCategory() != null ? s.getPracticeCategory() : "其他",
                Collectors.summingInt(PracticeSession::getDuration)
            ));

        // 转换为百分比
        Map<String, StatsDTO.CategoryStats> categoryStats = new HashMap<>();
        categoryMinutes.forEach((category, minutes) -> {
            int percentage = totalMinutes > 0 ? (minutes * 100 / totalMinutes) : 0;
            categoryStats.put(category, StatsDTO.CategoryStats.builder()
                .category(category)
                .minutes(minutes)
                .percentage(percentage)
                .build());
        });

        // 最近会话（取前10条）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<StatsDTO.SessionSummary> recentSessions = sessions.stream()
            .limit(10)
            .map(s -> StatsDTO.SessionSummary.builder()
                .date(s.getPracticeDate().format(formatter))
                .practiceName(s.getPracticeName())
                .duration(s.getDuration())
                .build())
            .collect(Collectors.toList());

        return StatsDTO.builder()
            .totalMinutes(totalMinutes)
            .totalHours(totalMinutes / 60.0)
            .sessionCount(sessions.size())
            .categoryStats(categoryStats)
            .recentSessions(recentSessions)
            .build();
    }

    private LocalDate calculateStartDate(LocalDate endDate, String range) {
        return switch (range) {
            case "today" -> endDate;
            case "week" -> endDate.minusWeeks(1);
            case "month" -> endDate.minusMonths(1);
            case "all" -> LocalDate.of(2000, 1, 1);
            default -> endDate.minusWeeks(1);
        };
    }
}
