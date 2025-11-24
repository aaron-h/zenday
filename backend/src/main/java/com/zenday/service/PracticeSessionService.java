package com.zenday.service;

import com.zenday.entity.Practice;
import com.zenday.entity.PracticeSession;
import com.zenday.entity.User;
import com.zenday.repository.PracticeRepository;
import com.zenday.repository.PracticeSessionRepository;
import com.zenday.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 修行会话服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PracticeSessionService {

    private final PracticeSessionRepository sessionRepository;
    private final PracticeRepository practiceRepository;
    private final UserRepository userRepository;

    /**
     * 创建修行会话
     */
    @Transactional
    public PracticeSession createSession(PracticeSession session) {
        log.info("Creating practice session for user: {}, practice: {}",
            session.getUserId(), session.getPracticeId());

        // 获取功课信息
        Practice practice = practiceRepository.findById(session.getPracticeId())
            .orElseThrow(() -> new RuntimeException("Practice not found: " + session.getPracticeId()));

        session.setPracticeName(practice.getName());
        session.setPracticeCategory(practice.getCategory());
        session.setPracticeDate(session.getStartTime().toLocalDate());

        PracticeSession saved = sessionRepository.save(session);

        // 更新用户累计时长
        updateUserTotalMinutes(session.getUserId());

        return saved;
    }

    /**
     * 完成修行会话
     */
    @Transactional
    public PracticeSession completeSession(Long sessionId, LocalDateTime endTime, String note) {
        log.info("Completing session: {}", sessionId);

        PracticeSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));

        session.setEndTime(endTime);
        session.setNote(note);

        // 计算实际时长
        long actualMinutes = java.time.Duration.between(session.getStartTime(), endTime).toMinutes();
        session.setActualDuration((int) actualMinutes);

        PracticeSession saved = sessionRepository.save(session);

        // 更新用户累计时长
        updateUserTotalMinutes(session.getUserId());

        return saved;
    }

    /**
     * 获取用户的会话历史
     */
    public List<PracticeSession> getUserSessions(Long userId) {
        return sessionRepository.findByUserIdOrderByStartTimeDesc(userId);
    }

    /**
     * 获取指定日期的会话
     */
    public List<PracticeSession> getSessionsByDate(Long userId, LocalDate date) {
        return sessionRepository.findByUserIdAndPracticeDateOrderByStartTimeDesc(userId, date);
    }

    /**
     * 获取日期范围内的会话
     */
    public List<PracticeSession> getSessionsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return sessionRepository.findByUserIdAndPracticeDateBetweenOrderByStartTimeDesc(
            userId, startDate, endDate);
    }

    /**
     * 更新用户累计时长
     */
    private void updateUserTotalMinutes(Long userId) {
        Integer totalMinutes = sessionRepository.sumDurationByUserId(userId);
        if (totalMinutes == null) {
            totalMinutes = 0;
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        user.setTotalMinutes(totalMinutes);
        userRepository.save(user);
    }
}
