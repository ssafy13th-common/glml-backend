package com.ssafy.a705.group.presentation.image;

import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.application.image.GroupImageApplicationService;
import com.ssafy.a705.group.presentation.image.dto.request.GroupImageCreateReq;
import com.ssafy.a705.group.presentation.image.dto.response.GroupImagesRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups/{group-id}/images")
public class GroupImageRestController {

    private final GroupImageApplicationService groupImageService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createGroupImage(
            @PathVariable("group-id") Long groupId,
            @RequestBody GroupImageCreateReq groupImageCreateReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        groupImageService.createGroupImage(groupId, groupImageCreateReq, userDetails);
        return ApiResponse.create();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<GroupImagesRes>> getImage(
            @PathVariable("group-id") Long groupId,
            @RequestParam(required = false) Long cursorId,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        GroupImagesRes groupImagesRes = groupImageService.getGroupImages(groupId, cursorId,
                pageable, userDetails);
        return ApiResponse.ok(groupImagesRes);
    }

    @DeleteMapping("/{group-image-id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroupImage(
            @PathVariable("group-id") Long groupId,
            @PathVariable("group-image-id") Long groupImageId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        groupImageService.deleteGroupImage(groupId, groupImageId, userDetails);
        return ApiResponse.ok();
    }

}
