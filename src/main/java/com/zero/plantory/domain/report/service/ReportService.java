package com.zero.plantory.domain.report.service;

import com.zero.plantory.domain.report.vo.SelectNameListVO;
import com.zero.plantory.global.vo.ImageVO;
import com.zero.plantory.global.vo.ReportVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    List<SelectNameListVO> getUsersIdByNickname(String nickname, Long viewerId);
    int registerReport(ReportVO reportVO, List<MultipartFile> files) throws IOException;
}
