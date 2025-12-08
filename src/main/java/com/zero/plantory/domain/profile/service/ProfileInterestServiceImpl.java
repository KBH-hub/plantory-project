package com.zero.plantory.domain.profile.service;

import com.zero.plantory.global.dto.ImageTargetType;
import com.zero.plantory.domain.image.mapper.ImageMapper;
import com.zero.plantory.domain.profile.dto.ProfileInterestListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingResponse;
import com.zero.plantory.domain.profile.mapper.ProfileSharingInsertMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileInterestServiceImpl implements ProfileInterestService {

    private final ProfileSharingInsertMapper profileSharingInsertMapper;
    private final ImageMapper imageMapper;

    @Override
    public List<ProfileSharingResponse> getProfileInterest(ProfileInterestListRequest req) {

        List<ProfileSharingResponse> list =
                profileSharingInsertMapper.selectInterestSharingList(
                        req.getMemberId(),
                        req.getKeyword(),
                        req.getLimit(),
                        req.getOffset()
                );

        list.forEach(item -> {
            String thumbnail = imageMapper.selectSharingThumbnail(
                    ImageTargetType.SHARING,
                    item.getSharingId()
            );
            item.setThumbnail(thumbnail);
        });

        return list;
    }
}
