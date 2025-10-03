package com.ssafy.a705.domain.group._participant.controller;

import com.ssafy.a705.domain.group._participant.dto.request.ParticipantsReq;
import com.ssafy.a705.domain.group._participant.dto.request.ParticipantsUpdateReq;
import com.ssafy.a705.domain.group._participant.dto.response.ParticipantsRes;
import com.ssafy.a705.domain.group._participant.service.ParticipantService;
import com.ssafy.a705.domain.group.service.GroupAggregateService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class ParticipantRestController {

    private final ParticipantService participantService;        // group 정보를 조회할 필요가 없는 경우에는 groupMemberService만 사용
    private final GroupAggregateService groupAggregateService;  // group 정보, member 정보를 조회할 필요가 있는 경우 groupAggregateService

    @GetMapping("/{group-id}/participants")
    public ResponseEntity<ApiResponse<ParticipantsRes>> getParticipants(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        log.info("멤바 조회");
        ParticipantsRes participants = participantService.getParticipants(groupId,
                customUserDetails);
        return ApiResponse.ok(participants);
    }


    @PostMapping("/{group-id}/participants")
    public ResponseEntity<ApiResponse<Void>> createGroupMember(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ParticipantsReq request) {
        groupAggregateService.createParticipants(groupId, request.emails(), customUserDetails);
        return ApiResponse.create();
    }

    @PutMapping("/{group-id}/participants")
    public ResponseEntity<ApiResponse<ParticipantsRes>> updateGroupMember(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ParticipantsUpdateReq request
    ) {
        ParticipantsRes participantsRes = participantService.updateParticipants(request,
                customUserDetails);
        return ApiResponse.ok(participantsRes);
    }

    @DeleteMapping("/{group-id}/members")
    public ResponseEntity<ApiResponse<Void>> deleteGroupMember(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody Map<String, String> email
    ) {
        groupAggregateService.deleteParticipant(groupId, email.get("email"), customUserDetails);
        return ApiResponse.ok();
    }
}
