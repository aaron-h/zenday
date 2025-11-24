package com.zenday.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功课DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PracticeDTO {

    private Long id;
    private String name;
    private String category;
    private Integer defaultDuration;
    private String defaultReminderTime;
    private String defaultSound;
    private Integer defaultVolume;
    private String defaultBreathingRhythm;
    private String icon;
    private String description;
    private Integer sortOrder;
    private Boolean enabled;
}
