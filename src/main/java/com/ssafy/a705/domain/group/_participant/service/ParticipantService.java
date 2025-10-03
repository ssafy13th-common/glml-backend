package com.ssafy.a705.domain.group._participant.service;

import com.ssafy.a705.domain.group._participant.dto.request.ParticipantUpdateReq;
import com.ssafy.a705.domain.group._participant.dto.request.ParticipantsUpdateReq;
import com.ssafy.a705.domain.group._participant.dto.response.ParticipantProfileRes;
import com.ssafy.a705.domain.group._participant.dto.response.ParticipantsRes;
import com.ssafy.a705.domain.group._participant.entity.Participant;
import com.ssafy.a705.domain.group._participant.entity.Role;
import com.ssafy.a705.domain.group._participant.exception.UnauthorizedParticipantException;
import com.ssafy.a705.domain.group._participant.repository.ParticipantRepository;
import com.ssafy.a705.domain.group._receipt.dto.request.SettlementReq;
import com.ssafy.a705.domain.group._receipt.dto.response.SettlementInfoRes;
import com.ssafy.a705.domain.group._receipt.dto.response.SettlementRes;
import com.ssafy.a705.domain.group.exception.GroupAccessDeniedException;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    /**
     * 그룹 멤버 목록 조회
     *
     * @param groupId           그룹 식별자
     * @param customUserDetails 인증된 유저 디테일
     * @return 그룹 멤버 목록 Response
     */
    @Transactional(readOnly = true)
    public ParticipantsRes getParticipants(Long groupId, CustomUserDetails customUserDetails) {
        memberAuthorityCheck(groupId, customUserDetails);

        List<Participant> participants = participantRepository.findMembersByGroupId(groupId);

        if (!adminCheck(participants)) {
            makeNewAdmin(participants);
        }

        return ParticipantsRes.from(groupId, participants);
    }

    public List<Participant> getParticipantEntities(Long groupId,
            CustomUserDetails customUserDetails) {
        memberAuthorityCheck(groupId, customUserDetails);
        return participantRepository.findMembersByGroupId(groupId);
    }

    /**
     * 중복 체크 (멤버 id와 그룹 id로 이미 존재하는지 판별)
     *
     * @param groupId           그룹 식별자
     * @param customUserDetails 인증된 유저 디테일
     * @return 존재 여부 반환(그룹에 이미 있다면 true, 없다면 false)
     */
    public boolean isExists(Long groupId, CustomUserDetails customUserDetails) {
        return participantRepository.isParticipantExists(customUserDetails.getId(), groupId);
    }

    public void checkMemberInGroup(Long groupId, CustomUserDetails customUserDetails) {
        if (!isExists(groupId, customUserDetails)) {
            throw new GroupAccessDeniedException();
        }
    }

    /**
     * email로 participantId Set을 받아오는 함수 (중복체크를 위함)
     *
     * @param groupId 그룹 식별자
     * @param emails  멤버 이메일 목록
     * @return 멤버 Id Set
     */
    @Transactional(readOnly = true)
    public Set<Long> getExistingEmails(Long groupId, List<String> emails) {
        return participantRepository.findExistingMemberIdsByEmails(groupId,
                emails);
    }

    /**
     * 그룹에 속한 멤버들의 Profile만 추출
     *
     * @param groupIds 그룹 식별자
     * @return 그룹 멤버 프로필 주소 리스트
     */
    @Transactional(readOnly = true)
    public List<ParticipantProfileRes> getParticipantProfiles(List<Long> groupIds) {
        return participantRepository.findProfilesByGroupIds(groupIds);
    }

    /**
     * 그룹에 속한 멤버를 삭제처리
     *
     * @param groupId           삭제하고자 하는 그룹 멤버가 속한 그룹
     * @param email             삭제하고자 하는 그룹 멤버의 이메일
     * @param customUserDetails 현재 로그인 된 유저의 정보
     */
    @Transactional
    public void deleteParticipant(Long groupId, String email,
            CustomUserDetails customUserDetails) {
        adminAuthorityCheck(groupId, customUserDetails);
        // 멤바 삭제
        Participant participant = participantRepository.getByMemberEmailAndGroupId(groupId, email);
        participant.deleteParticipant();
    }

    @Transactional
    public ParticipantsRes updateParticipants(ParticipantsUpdateReq request,
            CustomUserDetails customUserDetails) {
        memberAuthorityCheck(request.groupId(), customUserDetails); // 멤버에 속해있는지 권한 체크

        Map<Long, ParticipantUpdateReq> participantUpdateReqMap = request.updateParticipants().stream()
                .collect(Collectors.toMap(ParticipantUpdateReq::participantId,
                        ParticipantUpdateReq -> ParticipantUpdateReq)); // id와 ParticipantUpdateReq 매핑
        List<Long> participantsId = request.updateParticipants().stream()
                .map(ParticipantUpdateReq::participantId).toList();     // 일괄검색을 위한 participantIds
        List<Participant> participants = participantRepository.findByIdInAndDeletedAtIsNull(
                participantsId);    // DB에서 불러온 그룹 멤버 리스트

        for (Participant participant : participants) {
            ParticipantUpdateReq participantUpdate = participantUpdateReqMap.get(
                    participant.getId()); // 업데이트 정보 불러오기

            participant.updateAmount(participantUpdate.finalAmount(),
                    participantUpdate.lateFee());  // 업데이트
        }

        return ParticipantsRes.from(request.groupId(), participants);
    }

    @Transactional
    public SettlementRes updateParticipantsFinalAmount(Long groupId, SettlementReq settlementReq,
            CustomUserDetails userDetails) {
        memberAuthorityCheck(groupId, userDetails);
        List<SettlementInfoRes> settlements = settlementReq.memberEmails().stream()
                .map(memberEmail -> {
                    checkParticipantInGroup(groupId, memberEmail);
                    Participant participant = participantRepository.getByMemberEmailAndGroupId(
                            groupId, memberEmail);
                    participant.updateAmount(
                            participant.getFinalCost() + settlementReq.pricePerPerson(),
                            participant.getLateFee());
                    return SettlementInfoRes.of(memberEmail, participant.getFinalCost());
                }).toList();
        return SettlementRes.of(settlements);
    }

    @Transactional
    public void updateParticipantLateFee(Long groupId, Integer lateFee, String memberEmail) {
        Participant participant = participantRepository.getByMemberEmailAndGroupId(
                groupId, memberEmail);
        participant.updateAmount(participant.getFinalCost(), participant.getLateFee() + lateFee);
    }

    /**
     * 인증된 사용자의 관리자 권한 체크
     *
     * @param groupId           그룹 식별자
     * @param customUserDetails 인증된 사용자의 정보
     */
    @Transactional(readOnly = true)
    public void adminAuthorityCheck(Long groupId, CustomUserDetails customUserDetails) {
        Participant currentMember = memberAuthorityCheck(groupId, customUserDetails);
        if (currentMember.getRole() != Role.LEADER) { // 그룹 내의 역할 검증
            throw new UnauthorizedParticipantException();
        }
    }

    @Transactional(readOnly = true)
    public void checkParticipantInGroup(Long groupId, String memberEmail) {
        Participant participant = participantRepository.getByMemberEmailAndGroupId(
                groupId, memberEmail);
        if (Objects.isNull(participant)) {
            throw new GroupAccessDeniedException();
        }
    }

    /**
     * 인증된 사용자의 그룹 접근 권한 체크
     *
     * @param groupId           그룹 식별자
     * @param customUserDetails 인증된 유저 디테일
     * @return 그룹에 속한 멤버라면, 해당 멤버 반환
     */
    @Transactional(readOnly = true)
    public Participant memberAuthorityCheck(Long groupId, CustomUserDetails customUserDetails) {
        Participant participant = participantRepository.getByMemberIdAndGroupId(
                customUserDetails.getId(), groupId);

        if (participant == null) {
            throw new GroupAccessDeniedException();
        }
        return participant;
    }

    @Transactional
    public void saveParticipant(Participant participant) {
        participantRepository.save(participant);
    }

    @Transactional
    public void saveAllParticipants(List<Participant> participants) {
        participantRepository.saveAll(participants);
    }

    public boolean adminCheck(List<Participant> participants) {
        for (Participant participant : participants) {
            if (participant.getRole() == Role.LEADER) {
                return true;
            }
        }

        return false;
    }

    public void makeNewAdmin(List<Participant> participants) {
        Participant participant = participants.get(0);
        participant.upgradeToAdmin();
    }
}
