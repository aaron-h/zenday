package com.zenday.repository;

import com.zenday.entity.DailyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

    Optional<DailyPlan> findByUserIdAndPlanDate(Long userId, LocalDate planDate);

    List<DailyPlan> findByUserIdAndPlanDateBetweenOrderByPlanDateAsc(
        Long userId, LocalDate startDate, LocalDate endDate);

    boolean existsByUserIdAndPlanDate(Long userId, LocalDate planDate);
}
