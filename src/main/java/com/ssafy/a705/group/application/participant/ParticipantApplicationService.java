package com.ssafy.a705.group.application.participant;

import com.ssafy.a705.domain.chat.dto.ChatMemberInfoDTO;
import com.ssafy.a705.domain.chat.dto.request.AddMemberReq;
import com.ssafy.a705.domain.chat.dto.request.AddMembersReq;
import com.ssafy.a705.domain.chat.dto.request.RemoveMemberReq;
import com.ssafy.a705.domain.chat.service.ChatRoomService;
import com.ssafy.a705.group.presentation.receipt.dto.request.SettlementReq;
import com.ssafy.a705.group.presentation.receipt.dto.response.SettlementInfoRes;
import com.ssafy.a705.group.presentation.receipt.dto.response.SettlementRes;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.domain.group.entity.Group;
import com.ssafy.a705.group.domain.group.exception.GroupAccessDeniedException;
import com.ssafy.a705.group.domain.group.service.GroupReader;
import com.ssafy.a705.group.domain.participant.entity.Participant;
import com.ssafy.a705.group.domain.participant.entity.Role;
import com.ssafy.a705.group.domain.participant.exception.DuplicatedParticipantException;
import com.ssafy.a705.group.domain.participant.exception.UnauthorizedParticipantException;
import com.ssafy.a705.group.domain.participant.service.ParticipantReader;
import com.ssafy.a705.group.domain.participant.service.ParticipantStore;
import com.ssafy.a705.group.presentation.participant.dto.request.ParticipantUpdateReq;
import com.ssafy.a705.group.presentation.participant.dto.request.ParticipantsUpdateReq;
import com.ssafy.a705.group.presentation.participant.dto.response.ParticipantsRes;
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
public class ParticipantApplicationService {

    private final GroupReader groupReader;
    private final ParticipantReader participantReader;
    private final ParticipantStore participantStore;
    private final MemberRepository memberRepository;
    private final ChatRoomService chatRoomService;

    @Transactional
    public void createParticipants(Long groupId, List<String> emails, CustomUserDetails userDetails) {
        leaderAuthorityCheck(groupId, userDetails);
        Group group = groupReader.getGroup(groupId);
        List<Member> members = memberRepository.getAllByEmail(emails);
        Set<Long> existingParticipantIds = participantReader.getExistingEmails(groupId, emails);
        // 각 유저 별 중복 체크 후 생성
        List<Participant> newParticipants = members.stream()
                .filter(member -> !existingParticipantIds.contains(member.getId()))
                .map(member -> Participant.of(group, member, Role.MEMBER))
                .toList();

        // 중복 제거된 id와 nickname 맵 형성
        List<ChatMemberInfoDTO> infoList = newParticipants.stream()
                .map(groupMember -> ChatMemberInfoDTO.of(groupMember.getMember().getEmail(),
                        groupMember.getMember().getNickname())).toList();

        // 채팅방에 멤버 추가 및 저장
        chatRoomService.addMembers(
                AddMembersReq.of(group.getChatRoomId(), infoList));
        participantStore.saveAllParticipants(newParticipants);
    }

    @Transactional
    public void createGroupLeader(Long groupId, CustomUserDetails userDetails) {
        if (participantReader.isExists(groupId, userDetails.getId())) {
            throw new DuplicatedParticipantException();
        }
        Member member = memberRepository.getById(userDetails.getId());
        Group group = groupReader.getGroup(groupId);
        Participant groupMember = Participant.of(group, member, Role.LEADER);

        chatRoomService.addMember(
                AddMemberReq.of(group.getChatRoomId(), member.getEmail(), member.getNickname()));

        participantStore.saveParticipant(groupMember);
    }

    @Transactional(readOnly = true)
    public ParticipantsRes getParticipants(Long groupId, CustomUserDetails userDetails) {
        memberAuthorityCheck(groupId, userDetails);

        List<Participant> participants = participantReader.findMembersByGroupId(groupId);

        if (!leaderCheck(participants)) {
            makeNewLeader(participants);
        }

        return ParticipantsRes.from(groupId, participants);
    }

    @Transactional
    public ParticipantsRes updateParticipants(ParticipantsUpdateReq request, CustomUserDetails userDetails) {
        memberAuthorityCheck(request.groupId(), userDetails); // 멤버에 속해있는지 권한 체크

        Map<Long, ParticipantUpdateReq> participantUpdateReqMap = request.updateParticipants().stream()
                .collect(Collectors.toMap(ParticipantUpdateReq::participantId,
                        ParticipantUpdateReq -> ParticipantUpdateReq)); // id와 ParticipantUpdateReq 매핑
        List<Long> participantsId = request.updateParticipants().stream()
                .map(ParticipantUpdateReq::participantId).toList();     // 일괄검색을 위한 participantIds
        List<Participant> participants = participantReader.findByIdInAndDeletedAtIsNull(
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
    public SettlementRes updateParticipantsFinalAmount(Long groupId, SettlementReq settlementReq, CustomUserDetails userDetails) {
        memberAuthorityCheck(groupId, userDetails);
        List<SettlementInfoRes> settlements = settlementReq.memberEmails().stream()
                .map(memberEmail -> {
                    checkParticipantInGroup(groupId, memberEmail);
                    Participant participant = participantReader.getByMemberEmailAndGroupId(
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
        Participant participant = participantReader.getByMemberEmailAndGroupId(
                groupId, memberEmail);
        participant.updateAmount(participant.getFinalCost(), participant.getLateFee() + lateFee);
    }

    @Transactional
    public void deleteParticipant(Long groupId, String email,
                                  CustomUserDetails userDetails) {
        leaderAuthorityCheck(groupId, userDetails);

        Group group = groupReader.getGroup(groupId);

        List<ChatMemberInfoDTO> infoList = participantReader.findMembersByGroupId(groupId).stream().map(participant -> {
            Member m = participant.getMember();
            return ChatMemberInfoDTO.of(m.getEmail(), m.getNickname());
        }).toList();

        chatRoomService.removeMember(RemoveMemberReq.of(group.getChatRoomId(), infoList));

        Participant participant = participantReader.getByMemberEmailAndGroupId(groupId, email);
        participant.deleteParticipant();
    }

    @Transactional(readOnly = true)
    public void checkParticipantInGroup(Long groupId, String memberEmail) {
        Participant participant = participantReader.getByMemberEmailAndGroupId(
                groupId, memberEmail);
        if (Objects.isNull(participant)) {
            throw new GroupAccessDeniedException();
        }
    }

    public boolean leaderCheck(List<Participant> participants) {
        for (Participant participant : participants) {
            if (participant.getRole() == Role.LEADER) {
                return true;
            }
        }
        return false;
    }

    public void makeNewLeader(List<Participant> participants) {
        Participant participant = participants.get(0);
        participant.upgradeToLeader();
    }


    @Transactional(readOnly = true)
    public void leaderAuthorityCheck(Long groupId, CustomUserDetails userDetails) {
        Participant currentMember = memberAuthorityCheck(groupId, userDetails);
        if (currentMember.getRole() != Role.LEADER) { // 그룹 내의 역할 검증
            throw new UnauthorizedParticipantException();
        }
    }

    @Transactional(readOnly = true)
    public Participant memberAuthorityCheck(Long groupId, CustomUserDetails userDetails) {
        Participant participant = participantReader.getByMemberIdAndGroupId(
                userDetails.getId(), groupId);

        if (participant == null) {
            throw new GroupAccessDeniedException();
        }
        return participant;
    }

}
