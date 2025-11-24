package com.zenday.repository;

import com.zenday.entity.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {

    List<Practice> findByUserIdAndEnabledTrueOrderBySortOrderAsc(Long userId);

    List<Practice> findByUserIdOrderBySortOrderAsc(Long userId);

    List<Practice> findByUserIdAndCategoryOrderBySortOrderAsc(Long userId, String category);
}
