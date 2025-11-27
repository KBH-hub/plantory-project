package com.zero.plantory.domain.report.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.report.dto.NameListResponse;
import com.zero.plantory.domain.report.dto.ReportRequest;
import com.zero.plantory.domain.report.mapper.ReportMapper;
import com.zero.plantory.global.utils.StorageUploader;
import com.zero.plantory.global.dto.ImageTargetType;
import com.zero.plantory.global.dto.ImageDTO;
import lombok.RequiredArgsConstructor;
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
    public List<NameListResponse> getUsersIdByNickname(String nickname, Long viewerId) {
        List<NameListResponse> memberNameList = new ArrayList<>();
        for(NameListResponse member:reportMapper.selectUserIdByNickname(nickname)){
            if(Objects.equals(member.getMemberId(), viewerId)){
                continue;
            }
            memberNameList.add(member);
        }
        return memberNameList;
    }

    @Transactional
    @Override
    public int registerReport(ReportRequest request, List<MultipartFile> files) throws IOException {
        int result = 0;
        if(request.getTargetMemberId() == null){
            throw new IllegalArgumentException("신고하기 필수값(피신고자 아이디) 누락");
        }
        if(request.getContent() == null || request.getContent().isBlank()){
            throw new IllegalArgumentException("신고하기 필수값(신고 내용) 누락");
        }
        result += reportMapper.insertReport(request);

        for(MultipartFile file:files){
            String url = storageUploader.uploadFile(file);

            ImageDTO image = ImageDTO.builder()
                    .memberId(request.getReporterId())
                    .targetType(ImageTargetType.REPORT)
                    .targetId(request.getReportId())
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
