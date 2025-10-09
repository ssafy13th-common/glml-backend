package com.ssafy.a705.group.infrastructure.image.service;

import com.ssafy.a705.global.common.utils.S3PresignedUrlGenerator;
import com.ssafy.a705.group.domain.image.entity.GroupImage;
import com.ssafy.a705.group.domain.image.repository.GroupImageRepository;
import com.ssafy.a705.group.domain.image.service.GroupImageReader;
import com.ssafy.a705.group.presentation.image.dto.response.GroupImageRes;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupImageReaderImpl implements GroupImageReader {

    private final S3PresignedUrlGenerator uploader;
    private final GroupImageRepository groupImageRepository;

    @Override
    public List<GroupImageRes> getGroupImages(Long groupId, Long cursorId, int pageSize) {
        List<GroupImage> groupImages = groupImageRepository.findGroupImagesByGroupId(groupId,
                cursorId, pageSize);
        List<GroupImageRes> images = new ArrayList<>();
        for (GroupImage image : groupImages) {
            String imageUrl = uploader.generatePresignedGetUrl(image.getUrl());
            images.add(GroupImageRes.from(image, imageUrl));
        }
        return images;
    }

    @Override
    public GroupImage getByIdNotDeleted(Long groupImageId) {
        return groupImageRepository.getByIdNotDeleted(groupImageId);
    }
}
