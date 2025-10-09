package com.ssafy.a705.group.application.image;

import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.common.utils.S3PresignedUrlGenerator;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.application.participant.ParticipantApplicationService;
import com.ssafy.a705.group.domain.image.entity.GroupImage;
import com.ssafy.a705.group.domain.image.repository.GroupImageRepository;
import com.ssafy.a705.group.domain.image.service.GroupImageReader;
import com.ssafy.a705.group.domain.image.service.GroupImageStore;
import com.ssafy.a705.group.domain.participant.entity.Participant;
import com.ssafy.a705.group.presentation.image.dto.request.GroupImageCreateReq;
import com.ssafy.a705.group.presentation.image.dto.response.GroupImageRes;
import com.ssafy.a705.group.presentation.image.dto.response.GroupImagesRes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupImageApplicationService {

    private final ParticipantApplicationService participantService;
    private final GroupImageStore groupImageStore;
    private final GroupImageReader groupImageReader;

    @Transactional
    public void createGroupImage(Long groupId, GroupImageCreateReq imageCreateReq,
                                 CustomUserDetails userDetails) {
        Participant groupMember = participantService.memberAuthorityCheck(groupId, userDetails);
        for (String image : imageCreateReq.images()) {
            GroupImage groupImage = GroupImage.of(image, groupMember);
            groupImageStore.saveGroupImage(groupImage);
        }
    }

    @Transactional(readOnly = true)
    public GroupImagesRes getGroupImages(Long groupId, Long cursorId, Pageable pageable,
                                         CustomUserDetails userDetails) {
        participantService.memberAuthorityCheck(groupId, userDetails);
        List<GroupImageRes> groupImages = groupImageReader.getGroupImages(groupId, cursorId, pageable.getPageSize());
        return GroupImagesRes.of(groupImages);
    }

    @Transactional
    public void deleteGroupImage(Long groupId, Long groupImageId, CustomUserDetails userDetails) {
        Participant groupMember = participantService.memberAuthorityCheck(groupId, userDetails);
        GroupImage groupImage = groupImageReader.getByIdNotDeleted(groupImageId);
        checkImageHaveGroupMember(groupImage, groupMember);
        groupImage.deleteImage();
    }


    private void checkImageHaveGroupMember(GroupImage groupImage, Participant groupMember) {
        if (Objects.equals(groupImage.getParticipant(), groupMember)) {
            return;
        }

        throw new ForbiddenException("그룹 접근");
    }
}
