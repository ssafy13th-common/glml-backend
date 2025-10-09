package com.ssafy.a705.group.presentation.participant;

import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.application.participant.ParticipantApplicationService;
import com.ssafy.a705.group.presentation.participant.dto.request.ParticipantsReq;
import com.ssafy.a705.group.presentation.participant.dto.request.ParticipantsUpdateReq;
import com.ssafy.a705.group.presentation.participant.dto.response.ParticipantsRes;
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
@RequestMapping("/api/v1/groups/{group-id}/participants")
public class ParticipantRestController {

    private final ParticipantApplicationService participantService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createParticipant(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ParticipantsReq request) {
        participantService.createParticipants(groupId, request.emails(), userDetails);
        return ApiResponse.create();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ParticipantsRes>> getParticipants(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ParticipantsRes participants = participantService.getParticipants(groupId, userDetails);
        return ApiResponse.ok(participants);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ParticipantsRes>> updateParticipant(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ParticipantsUpdateReq request
    ) {
        ParticipantsRes participantsRes = participantService.updateParticipants(request,
                userDetails);
        return ApiResponse.ok(participantsRes);
    }

    // TODO dto가 아니라 map으로 받고 있습니다.
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteParticipant(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, String> email
    ) {
        participantService.deleteParticipant(groupId, email.get("email"), userDetails);
        return ApiResponse.ok();
    }
}
