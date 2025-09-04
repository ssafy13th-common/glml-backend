package com.ssafy.a705.domain.group.service;

import com.ssafy.a705.domain.chat.dto.ChatMemberInfoDTO;
import com.ssafy.a705.domain.chat.dto.request.AddMemberReq;
import com.ssafy.a705.domain.chat.dto.request.AddMembersReq;
import com.ssafy.a705.domain.chat.dto.request.CreateRoomReq;
import com.ssafy.a705.domain.chat.dto.request.RemoveMemberReq;
import com.ssafy.a705.domain.chat.dto.response.ChatRoomRes;
import com.ssafy.a705.domain.chat.service.ChatRoomService;
import com.ssafy.a705.domain.group._member.dto.response.GroupMemberProfileRes;
import com.ssafy.a705.domain.group._member.entity.GroupMember;
import com.ssafy.a705.domain.group._member.entity.Role;
import com.ssafy.a705.domain.group._member.exception.DuplicatedGroupMemberException;
import com.ssafy.a705.domain.group._member.service.GroupMemberService;
import com.ssafy.a705.domain.group.dto.request.GroupReq;
import com.ssafy.a705.domain.group.dto.request.GroupUpdateReq;
import com.ssafy.a705.domain.group.dto.response.GroupGatheringRes;
import com.ssafy.a705.domain.group.dto.response.GroupInfoRes;
import com.ssafy.a705.domain.group.dto.response.GroupsRes;
import com.ssafy.a705.domain.group.entity.Group;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupAggregateService {

    private final MemberRepository memberRepository;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final ChatRoomService chatRoomService;              // 채팅방 service

    /**
     * 그룹 생성
     *
     * @param groupReq          그룹 생성 Request
     * @param customUserDetails 인증된 유저 디테일
     * @return 생성된 그룹 정보
     */
    public GroupInfoRes createGroupWithAdminAndMembers(GroupReq groupReq,
            CustomUserDetails customUserDetails) {
        // 1. 그룹 생성
        Group createdGroup = groupService.createGroup(groupReq);

        // 1-2. 빈 멤버 목록을 가진 채팅방 생성
        ChatRoomRes roomRes = chatRoomService.createChatRoom(
                CreateRoomReq.of(createdGroup.getName(), new ArrayList<>()),
                customUserDetails);

        // 1-3. 그룹에 chatRoomId 추가 후 저장
        createdGroup.updateChatRoomId(roomRes.roomId());
        groupService.saveGroup(createdGroup);

        // 2. 현재 사용자 Admin으로 추가 및 채팅방에 추가
        createGroupAdmin(createdGroup.getId(), customUserDetails);

        // 3. GroupMember가 있다면 Member 추가 및 채팅방에 추가
        if (groupReq.members() != null && !groupReq.members().isEmpty()) {
            createGroupMembers(createdGroup.getId(), groupReq.members(),
                    customUserDetails);
        }

        return GroupInfoRes.from(createdGroup);
    }

    /**
     * 그룹 정보
     *
     * @param groupId           그룹 식별자
     * @param customUserDetails 인증된 유저 디테일
     * @return 그룹 상세 정보 Response
     */
    public GroupInfoRes getGroup(Long groupId, CustomUserDetails customUserDetails) {
        groupMemberService.memberAuthorityCheck(groupId, customUserDetails);    // 그룹 접근 권한 체크
        Group group = groupService.getGroup(groupId);        // 그룹 불러오기
        return GroupInfoRes.from(group);
    }

    /**
     * 그룹 목록
     *
     * @param customUserDetails 인증된 유저 정보
     * @return 로그인된 유저가 가입되어 있는 그룹 목록 Response
     */
    public GroupsRes getGroups(CustomUserDetails customUserDetails) {
        if (Objects.isNull(customUserDetails)) {
            return GroupsRes.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        List<Group> groups = groupService.getGroups(customUserDetails); // 그룹 목록 불러오기
        List<Long> groupIds = groups.stream().map(Group::getId).toList();
        List<GroupMemberProfileRes> profileDtos = groupMemberService.getGroupMemberProfiles(
                groupIds); // 그룹 멤버 프로필만 추출

        return GroupsRes.of(groups, groupIds, profileDtos);
    }

    /**
     * 그룹 정보 업데이트
     *
     * @param groupId           그룹 식별자
     * @param groupUpdateReq    그룹 업데이트 Request
     * @param customUserDetails 인증된 유저 디테일
     * @return 그룹 상세 정보
     */
    public GroupInfoRes updateGroup(Long groupId, GroupUpdateReq groupUpdateReq,
            CustomUserDetails customUserDetails) {
        groupMemberService.adminAuthorityCheck(groupId, customUserDetails);     // 권한체크

        Group updatedGroup = groupService.updateGroup(groupId, groupUpdateReq); // 업데이트

        return GroupInfoRes.from(updatedGroup);
    }

    /**
     * 그룹 삭제 처리
     *
     * @param groupId           그룹 식별자
     * @param customUserDetails 인증된 유저 디테일
     */
    public void deleteGroup(Long groupId, CustomUserDetails customUserDetails) {
        groupMemberService.adminAuthorityCheck(groupId, customUserDetails); // 권한체크
        groupService.deleteGroup(groupId); // 삭제처리
    }

    @Transactional(readOnly = true)
    public GroupGatheringRes getGroupGatheringInfo(Long groupId,
            CustomUserDetails customUserDetails) {
        groupMemberService.memberAuthorityCheck(groupId, customUserDetails);

        Group group = groupService.getGroup(groupId);
        return GroupGatheringRes.from(group);
    }

    /**
     * 그룹에 어드민 추가 (현재 유저 기준) - 이것은 최초 그룹 생성시에만 사용합니다 어드민을 그룹에 추가 후 채팅방에도 추가함
     *
     * @param groupId           그룹 식별자
     * @param customUserDetails 인증된 유저 디테일
     */
    public void createGroupAdmin(Long groupId,
            CustomUserDetails customUserDetails) {
        if (groupMemberService.isExists(groupId, customUserDetails)) {
            throw new DuplicatedGroupMemberException();
        }
        Member member = memberRepository.getById(customUserDetails.getId());
        Group group = groupService.getGroup(groupId);
        GroupMember groupMember = GroupMember.of(group, member, Role.ADMIN);

        chatRoomService.addMember(
                AddMemberReq.of(group.getChatRoomId(), member.getEmail(), member.getNickname()));

        groupMemberService.saveMember(groupMember);
    }

    /**
     * 그룹에 멤버 여러 명 추가 - 그룹 멤버 여러명 생성 및 DB 저장
     *
     * @param groupId 멤버를 추가하려는 그룹의 ID
     * @param emails  유저 이메일 목록을 담고 있는 리스트
     */
    public void createGroupMembers(Long groupId,
            List<String> emails, CustomUserDetails customUserDetails) {

        groupMemberService.adminAuthorityCheck(groupId, customUserDetails); // 권한 체크

        Group group = groupService.getGroup(groupId);
        List<Member> members = memberRepository.getAllByEmail(emails);
        Set<Long> existingMemberIds = groupMemberService.getExistingEmails(groupId, emails);
        // 각 유저 별 중복 체크 후 생성
        List<GroupMember> newGroupMembers = members.stream()
                .filter(member -> !existingMemberIds.contains(member.getId()))
                .map(member -> GroupMember.of(group, member, Role.MEMBER))
                .toList();

        // 중복 제거된 id와 nickname 맵 형성
        List<ChatMemberInfoDTO> infoList = newGroupMembers.stream()
                .map(groupMember -> ChatMemberInfoDTO.of(groupMember.getMember().getEmail(),
                        groupMember.getMember().getNickname())).toList();

        // 채팅방에 멤버 추가 및 저장
        chatRoomService.addMembers(
                AddMembersReq.of(group.getChatRoomId(), infoList));
        groupMemberService.saveAllMembers(newGroupMembers);
    }

    public void deleteGroupMember(Long groupId, String email,
            CustomUserDetails customUserDetails) {
        groupMemberService.deleteGroupMember(groupId, email, customUserDetails);
        Group group = groupService.getGroup(groupId);

        List<ChatMemberInfoDTO> infoList = groupMemberService.getGroupMemberEntities(groupId,
                customUserDetails).stream().map(groupMember -> {
            Member m = groupMember.getMember();
            return ChatMemberInfoDTO.of(m.getEmail(), m.getNickname());
        }).toList();

        chatRoomService.removeMember(RemoveMemberReq.of(group.getChatRoomId(), infoList));
    }
}
