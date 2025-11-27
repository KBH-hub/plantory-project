package com.zero.plantory.domain.sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    private Long commentId;
    private Long sharingId;
    private Long writerId;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private Date delFlag;
}
