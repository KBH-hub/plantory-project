package com.zero.plantory.domain.sharing.vo;

import com.zero.plantory.global.vo.ImageVO;
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
public class SelectSharingDetailVO {
    private Long sharingId;
    private Long memberId;
    private String nickname;
    private Integer sharingRate;

    private String title;
    private String content;
    private String plantType;
    private String managementLevel;
    private String managementNeeds;
    private Integer interestNum;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ImageVO> images;

}
