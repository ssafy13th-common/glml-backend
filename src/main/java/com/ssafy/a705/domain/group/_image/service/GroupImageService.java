package com.ssafy.a705.domain.group._image.service;

import com.ssafy.a705.domain.group._image.dto.request.GroupImageCreateReq;
import com.ssafy.a705.domain.group._image.dto.response.GroupImageRes;
import com.ssafy.a705.domain.group._image.dto.response.GroupImagesRes;
import com.ssafy.a705.domain.group._image.entity.GroupImage;
import com.ssafy.a705.domain.group._image.repository.GroupImageRepository;
import com.ssafy.a705.domain.group._member.entity.GroupMember;
import com.ssafy.a705.domain.group._member.service.GroupMemberService;
import com.ssafy.a705.domain.group.service.GroupService;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.common.utils.S3PresignedUploader;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupImageService {

    private final GroupService groupService;
    private final S3PresignedUploader uploader;
    private final GroupImageRepository imageRepository;
    private final GroupMemberService groupMemberService;

    @Transactional
    public void createGroupImage(Long groupId, GroupImageCreateReq imageCreateReq,
            CustomUserDetails userDetails) {
        GroupMember groupMember = groupMemberService.memberAuthorityCheck(groupId, userDetails);
        for (String image : imageCreateReq.images()) {
            GroupImage groupImage = GroupImage.of(image, groupMember);
            imageRepository.save(groupImage);
        }
    }

    @Transactional
    public void deleteGroupImage(Long groupId, Long groupImageId, CustomUserDetails userDetails) {
        GroupMember groupMember = groupMemberService.memberAuthorityCheck(groupId, userDetails);
        GroupImage groupImage = imageRepository.getByIdNotDeleted(groupImageId);
        checkImageHaveGroupMember(groupImage, groupMember);
        groupImage.deleteImage();
    }

    @Transactional(readOnly = true)
    public GroupImagesRes getGroupImages(Long groupId, Long cursorId, Pageable pageable,
            CustomUserDetails userDetails) {
        groupMemberService.memberAuthorityCheck(groupId, userDetails);
        List<GroupImage> groupImages = imageRepository.findGroupImagesByGroupId(groupId,
                cursorId, pageable.getPageSize());

        List<GroupImageRes> images = new ArrayList<>();
        for (GroupImage image : groupImages) {
            String imageUrl = uploader.generatePresignedGetUrl(image.getImageUrl());
            images.add(GroupImageRes.from(image, imageUrl));
        }

        return GroupImagesRes.of(images);
    }

    private void checkImageHaveGroupMember(GroupImage groupImage, GroupMember groupMember) {
        if (Objects.equals(groupImage.getGroupMember(), groupMember)) {
            return;
        }

        throw new ForbiddenException("그룹 접근");
    }
}
