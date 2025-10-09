package com.ssafy.a705.group.presentation.image.dto.response;

import java.util.List;

public record GroupImagesRes(
        List<GroupImageRes> images
) {

    public static GroupImagesRes of(List<GroupImageRes> images) {
        return new GroupImagesRes(images);
    }
}
