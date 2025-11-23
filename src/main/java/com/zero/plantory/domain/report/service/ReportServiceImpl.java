package com.zero.plantory.domain.report.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.report.mapper.ReportMapper;
import com.zero.plantory.domain.report.vo.SelectNameListVO;
import com.zero.plantory.global.utils.StorageUploader;
import com.zero.plantory.global.vo.ImageTargetType;
import com.zero.plantory.global.vo.ImageVO;
import com.zero.plantory.global.vo.ReportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportMapper reportMapper;
    private final ImageMapper imageMapper;
    private final StorageUploader storageUploader;

    @Override
    public List<SelectNameListVO> getUsersIdByNickname(String nickname, Long viewerId) {
        List<SelectNameListVO> memberNameList = new ArrayList<>();
        for(SelectNameListVO member:reportMapper.selectUserIdByNickname(nickname)){
            if(Objects.equals(member.getMemberId(), viewerId)){
                continue;
            }
            memberNameList.add(member);
        }
        return memberNameList;
    }

    @Transactional
    @Override
    public int registerReport(ReportVO reportVO, List<MultipartFile> files) throws IOException {
        int result = 0;
        if(reportVO.getTargetMemberId() == null){
            throw new IllegalArgumentException("신고하기 필수값(피신고자 아이디) 누락");
        }
        if(reportVO.getContent() == null || reportVO.getContent().isBlank()){
            throw new IllegalArgumentException("신고하기 필수값(신고 내용) 누락");
        }
        result += reportMapper.insertReport(reportVO);

        for(MultipartFile file:files){
            String url = storageUploader.uploadFile(file);

            ImageVO image = ImageVO.builder()
                    .memberId(reportVO.getReporterId())
                    .targetType(ImageTargetType.REPORT)
                    .targetId(reportVO.getReportId())
                    .fileUrl(url)
                    .fileName(file.getOriginalFilename())
                    .build();
            result += imageMapper.insertImage(image);

        }

        if(result != files.size()+1)
            throw new IllegalStateException("신고 실패(저장 중 누락 발생)");

        return result;
    }
}
