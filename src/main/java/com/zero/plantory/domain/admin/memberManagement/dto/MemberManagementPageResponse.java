package com.zero.plantory.domain.admin.memberManagement.dto;

import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementResponse;
import com.zero.plantory.domain.profile.dto.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberManagementPageResponse {
    private int totalCount;
    private List<MemberResponse> list;
}
