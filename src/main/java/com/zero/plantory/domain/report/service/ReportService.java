package com.zero.plantory.domain.report.service;

import com.zero.plantory.domain.report.dto.NameListResponse;
import com.zero.plantory.domain.report.dto.ReportRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    List<NameListResponse> getUsersIdByNickname(String nickname, Long viewerId);
    int registerReport(ReportRequest request, List<MultipartFile> files) throws IOException;
}
