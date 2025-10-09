package com.ssafy.a705.group.presentation.image.dto.response;

import com.ssafy.a705.group.domain.image.entity.GroupImage;

public record GroupImageRes(
        Long imageId,
        String imageUrl,
        String memberEmail
) {

    public static GroupImageRes from(GroupImage groupImage, String url) {
        return new GroupImageRes(
                groupImage.getId(),
                url,
                groupImage.getParticipant().getMember().getEmail()
        );
    }

}
