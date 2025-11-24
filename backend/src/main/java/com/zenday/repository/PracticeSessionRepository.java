package com.zenday.repository;

import com.zenday.entity.PracticeSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PracticeSessionRepository extends JpaRepository<PracticeSession, Long> {

    List<PracticeSession> findByUserIdOrderByStartTimeDesc(Long userId);

    List<PracticeSession> findByUserIdAndPracticeDateOrderByStartTimeDesc(
        Long userId, LocalDate practiceDate);

    List<PracticeSession> findByUserIdAndPracticeDateBetweenOrderByStartTimeDesc(
        Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(s.duration) FROM PracticeSession s WHERE s.userId = :userId")
    Integer sumDurationByUserId(Long userId);

    @Query("SELECT SUM(s.duration) FROM PracticeSession s " +
           "WHERE s.userId = :userId AND s.practiceDate = :date")
    Integer sumDurationByUserIdAndDate(Long userId, LocalDate date);

    @Query("SELECT SUM(s.duration) FROM PracticeSession s " +
           "WHERE s.userId = :userId AND s.practiceDate BETWEEN :startDate AND :endDate")
    Integer sumDurationByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT s.practiceCategory, SUM(s.duration) FROM PracticeSession s " +
           "WHERE s.userId = :userId AND s.practiceDate BETWEEN :startDate AND :endDate " +
           "GROUP BY s.practiceCategory")
    List<Object[]> sumDurationByUserIdAndDateBetweenGroupByCategory(
        Long userId, LocalDate startDate, LocalDate endDate);
}
