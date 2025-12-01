package com.zero.plantory.domain.admin.reportManagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class IdListRequest {
    private List<Long> ids;
}

