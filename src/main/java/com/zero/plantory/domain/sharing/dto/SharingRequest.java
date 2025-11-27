package com.zero.plantory.domain.sharing.dto;

import com.zero.plantory.global.vo.ManagementLevel;
import com.zero.plantory.global.vo.ManagementNeeds;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharingRequest {
    private Long sharingId;
    private Long memberId;
    private Long targetMemberId;
    private String title;
    private String content;
    private String plantType;
    private ManagementLevel managementLevel;
    private ManagementNeeds managementNeeds;
    private Integer interestNum;
    private String status;
    private LocalDateTime reviewFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime delFlag;
}
