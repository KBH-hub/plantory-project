package com.zero.plantory.domain.sharing.dto;

import com.zero.plantory.global.dto.ManageLevel;
import com.zero.plantory.global.dto.ManageDemand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private ManageLevel managementLevel;
    private ManageDemand managementNeeds;
    private Integer interestNum;
    private String status;
    private LocalDateTime reviewFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime delFlag;

    private String deletedImageIds;
    private List<Long> deletedImageIdList;
}
