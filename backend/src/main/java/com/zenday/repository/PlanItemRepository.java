package com.zenday.repository;

import com.zenday.entity.PlanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanItemRepository extends JpaRepository<PlanItem, Long> {

    List<PlanItem> findByPlanIdOrderBySortOrderAsc(Long planId);

    void deleteByPlanId(Long planId);

    long countByPlanIdAndCompletedTrue(Long planId);
}
