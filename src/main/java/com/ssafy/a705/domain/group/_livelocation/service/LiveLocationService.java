package com.ssafy.a705.domain.group._livelocation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.a705.domain.group._livelocation.dto.GroupGatheringDto;
import com.ssafy.a705.domain.group._livelocation.dto.LiveLocationReq;
import com.ssafy.a705.domain.group._livelocation.dto.LiveLocationRes;
import com.ssafy.a705.domain.group._livelocation.repository.LiveLocationRedisRepository;
import com.ssafy.a705.domain.group._member.service.GroupMemberService;
import com.ssafy.a705.global.common.utils.GeoUtils;
import com.ssafy.a705.global.security.websocket.WebSocketSessionManager;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiveLocationService {

    private final GroupGatheringService groupGatheringService;
    private final LiveLocationRedisRepository liveLocationRedisRepository;
    private final LiveLocationStatusService liveLocationStatusService;
    private final WebSocketSessionManager sessionManager;
    private final GroupMemberService groupMemberService;
    private final ObjectMapper objectMapper;

    private static final double ARRIVE_APPROVED_RADIUS = 50.0;

    public void handleLocationUpdate(LiveLocationReq liveLocationReq,
            String memberEmail) {
        if (!liveLocationStatusService.isEnabledStatus(liveLocationReq.groupId(),
                memberEmail)) {
            return;
        }
        GroupGatheringDto groupGathering = groupGatheringService.getGroupGathering(
                liveLocationReq.groupId()).orElse(null);

        if (Objects.isNull(groupGathering)) {
            log.warn("그룹 모임 정보 없음: groupId={}", liveLocationReq.groupId());
            return;
        }
        liveLocationRedisRepository.addPendingMember(liveLocationReq.groupId(), memberEmail);
        boolean arrived = isArrived(groupGathering, liveLocationReq);
        int lateFee = calculateLateFee(liveLocationReq.timestamp(), groupGathering.gatheringTime(),
                groupGathering.feePerMinute());

        LiveLocationRes liveLocationRes = LiveLocationRes.from(liveLocationReq, memberEmail,
                lateFee);
        liveLocationRedisRepository.saveLocation(liveLocationRes);
        if (arrived) {
            groupMemberService.updateGroupMemberLateFee(liveLocationReq.groupId(), lateFee,
                    memberEmail);
            liveLocationRedisRepository.removePendingMember(liveLocationReq.groupId(), memberEmail);
            if (liveLocationRedisRepository.countPendingMembers(liveLocationReq.groupId()) == 0L) {
                sessionManager.closeAllInGroup(liveLocationReq.groupId());
                liveLocationRedisRepository.cleanupGroupAllKeys(liveLocationReq.groupId());
                return;
            }
        }
        broadCastLocationInGroup(liveLocationRes);
    }

    public void cleanup(Long groupId, String memberEmail) {
        liveLocationStatusService.disableStatus(groupId, memberEmail);
        if (liveLocationRedisRepository.countPendingMembers(groupId) == 0L) {
            liveLocationRedisRepository.cleanupGroupAllKeys(groupId);
        }
    }

    private boolean isArrived(GroupGatheringDto groupGathering,
            LiveLocationReq liveLocationReq) {
        return GeoUtils.calculateHaversine(groupGathering.latitude(),
                groupGathering.longitude(), liveLocationReq.latitude(),
                liveLocationReq.longitude()) <= ARRIVE_APPROVED_RADIUS;
    }

    private int calculateLateFee(LocalDateTime memberTime,
            LocalDateTime gatheringTime, int feePerMinute) {
        long minutes = Math.max(0, Duration.between(gatheringTime,
                memberTime).toMinutes());
        if (minutes == 0) {
            return 0;
        }
        return Math.toIntExact(minutes * (long) feePerMinute);
    }

    private void broadCastLocationInGroup(LiveLocationRes liveLocationRes) {
        sessionManager.getEmailsInGroup(liveLocationRes.groupId()).forEach(email -> {
            sessionManager.getSession(email).ifPresent(session -> {
                log.info("broadCastLocationInGroup session {}", session);
                if (session.isOpen()) {
                    try {
                        session.sendMessage(
                                new TextMessage(objectMapper.writeValueAsString(liveLocationRes)));
                    } catch (Exception e) {
                        log.warn("위치 전송 실패 -> 세션 정리: {}", session.getId(), e);
                        sessionManager.unregisterSessionBySessionId(session.getId());
                    }
                } else {
                    sessionManager.unregisterSessionBySessionId(session.getId());
                }
            });
        });
    }
}