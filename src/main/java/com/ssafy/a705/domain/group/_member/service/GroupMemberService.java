package com.ssafy.a705.domain.group._member.service;

import com.ssafy.a705.domain.group._member.dto.request.GroupMemberUpdateReq;
import com.ssafy.a705.domain.group._member.dto.request.GroupMembersUpdateReq;
import com.ssafy.a705.domain.group._member.dto.response.GroupMemberProfileRes;
import com.ssafy.a705.domain.group._member.dto.response.GroupMembersRes;
import com.ssafy.a705.domain.group._member.entity.GroupMember;
import com.ssafy.a705.domain.group._member.entity.Role;
import com.ssafy.a705.domain.group._member.exception.UnauthorizedGroupMemberException;
import com.ssafy.a705.domain.group._member.repository.GroupMemberRepository;
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
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;

    /**
     * 그룹 멤버 목록 조회
     *
     * @param groupId           그룹 식별자
     * @param customUserDetails 인증된 유저 디테일
     * @return 그룹 멤버 목록 Response
     */
    @Transactional(readOnly = true)
    public GroupMembersRes getGroupMembers(Long groupId, CustomUserDetails customUserDetails) {
        memberAuthorityCheck(groupId, customUserDetails);

        List<GroupMember> groupMembers = groupMemberRepository.findMembersByGroupId(groupId);

        if (!adminCheck(groupMembers)) {
            makeNewAdmin(groupMembers);
        }

        return GroupMembersRes.of(groupId, groupMembers);
    }

    public List<GroupMember> getGroupMemberEntities(Long groupId,
            CustomUserDetails customUserDetails) {
        memberAuthorityCheck(groupId, customUserDetails);

        List<GroupMember> groupMembers = groupMemberRepository.findMembersByGroupId(groupId);
        return groupMembers;
    }

    /**
     * 중복 체크 (멤버 id와 그룹 id로 이미 존재하는지 판별)
     *
     * @param groupId           그룹 식별자
     * @param customUserDetails 인증된 유저 디테일
     * @return 존재 여부 반환(그룹에 이미 있다면 true, 없다면 false)
     */
    public boolean isExists(Long groupId, CustomUserDetails customUserDetails) {
        return groupMemberRepository.isGroupMemberExists(customUserDetails.getId(), groupId);
    }

    public void checkMemberInGroup(Long groupId, CustomUserDetails customUserDetails) {
        if (!isExists(groupId, customUserDetails)) {
            throw new GroupAccessDeniedException();
        }
    }


    /**
     * email로 groupMemberId Set을 받아오는 함수 (중복체크를 위함)
     *
     * @param groupId 그룹 식별자
     * @param emails  멤버 이메일 목록
     * @return 멤버 Id Set
     */
    @Transactional(readOnly = true)
    public Set<Long> getExistingEmails(Long groupId, List<String> emails) {
        return groupMemberRepository.findExistingMemberIdsByEmails(groupId,
                emails);
    }

    /**
     * 그룹에 속한 멤버들의 Profile만 추출
     *
     * @param groupIds 그룹 식별자
     * @return 그룹 멤버 프로필 주소 리스트
     */
    @Transactional(readOnly = true)
    public List<GroupMemberProfileRes> getGroupMemberProfiles(List<Long> groupIds) {
        List<GroupMemberProfileRes> profileDtos = groupMemberRepository.findProfilesByGroupIds(
                groupIds);
        return profileDtos;
    }

    /**
     * 그룹에 속한 멤버를 삭제처리
     *
     * @param groupId           삭제하고자 하는 그룹 멤버가 속한 그룹
     * @param email             삭제하고자 하는 그룹 멤버의 이메일
     * @param customUserDetails 현재 로그인 된 유저의 정보
     */
    @Transactional
    public void deleteGroupMember(Long groupId, String email,
            CustomUserDetails customUserDetails) {
        adminAuthorityCheck(groupId, customUserDetails);
        // 멤바 삭제
        GroupMember member = groupMemberRepository.getByMemberEmailAndGroupId(groupId, email);
        member.deleteGroupMember();
    }

    @Transactional
    public GroupMembersRes updateGroupMembers(GroupMembersUpdateReq request,
            CustomUserDetails customUserDetails) {
        memberAuthorityCheck(request.groupId(), customUserDetails); // 멤버에 속해있는지 권한 체크

        Map<Long, GroupMemberUpdateReq> groupMemberUpdateReqMap = request.updateMembers().stream()
                .collect(Collectors.toMap(GroupMemberUpdateReq::groupMemberId,
                        GroupMemberUpdateReq -> GroupMemberUpdateReq)); // id와 GroupMemberUpdateReq 매핑
        List<Long> groupMemberIds = request.updateMembers().stream()
                .map(GroupMemberUpdateReq::groupMemberId).toList();     // 일괄검색을 위한 groupMemberIds
        List<GroupMember> groupMembers = groupMemberRepository.findGroupMembersByGroupMemberIds(
                groupMemberIds);    // DB에서 불러온 그룹 멤버 리스트

        for (GroupMember groupMember : groupMembers) {
            GroupMemberUpdateReq groupMemberUpdate = groupMemberUpdateReqMap.get(
                    groupMember.getId()); // 업데이트 정보 불러오기

            groupMember.updateAmount(groupMemberUpdate.finalAmount(),
                    groupMemberUpdate.lateFee());  // 업데이트
        }

        return GroupMembersRes.of(request.groupId(), groupMembers);
    }

    @Transactional
    public SettlementRes updateGroupMembersFinalAmount(Long groupId, SettlementReq settlementReq,
            CustomUserDetails userDetails) {
        memberAuthorityCheck(groupId, userDetails);
        List<SettlementInfoRes> settlements = settlementReq.memberEmails().stream()
                .map(memberEmail -> {
                    checkGroupMemberInGroup(groupId, memberEmail);
                    GroupMember groupMember = groupMemberRepository.getByMemberEmailAndGroupId(
                            groupId, memberEmail);
                    groupMember.updateAmount(
                            groupMember.getFinalAmount() + settlementReq.pricePerPerson(),
                            groupMember.getLateFee());
                    return SettlementInfoRes.of(memberEmail, groupMember.getFinalAmount());
                }).toList();
        return SettlementRes.of(settlements);
    }

    @Transactional
    public void updateGroupMemberLateFee(Long groupId, Integer lateFee, String memberEmail) {
        GroupMember groupMember = groupMemberRepository.getByMemberEmailAndGroupId(
                groupId, memberEmail);
        groupMember.updateAmount(groupMember.getFinalAmount(), groupMember.getLateFee() + lateFee);
    }

    /**
     * 인증된 사용자의 관리자 권한 체크
     *
     * @param groupId           그룹 식별자
     * @param customUserDetails 인증된 사용자의 정보
     */
    @Transactional(readOnly = true)
    public void adminAuthorityCheck(Long groupId, CustomUserDetails customUserDetails) {
        GroupMember currentMember = memberAuthorityCheck(groupId, customUserDetails);
        if (currentMember.getRole() != Role.ADMIN) { // 그룹 내의 역할 검증
            throw new UnauthorizedGroupMemberException();
        }
    }

    @Transactional(readOnly = true)
    public void checkGroupMemberInGroup(Long groupId, String memberEmail) {
        GroupMember groupMember = groupMemberRepository.getByMemberEmailAndGroupId(
                groupId, memberEmail);
        if (Objects.isNull(groupMember)) {
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
    public GroupMember memberAuthorityCheck(Long groupId, CustomUserDetails customUserDetails) {
        GroupMember currentMember = groupMemberRepository.getByMemberIdAndGroupId(
                customUserDetails.getId(), groupId);

        if (currentMember == null) {
            throw new GroupAccessDeniedException();
        }
        return currentMember;
    }

    @Transactional
    public void saveMember(GroupMember groupMember) {
        groupMemberRepository.save(groupMember);
    }

    @Transactional
    public void saveAllMembers(List<GroupMember> groupMembers) {
        groupMemberRepository.saveAll(groupMembers);
    }

    public boolean adminCheck(List<GroupMember> groupMembers) {
        for (GroupMember gm : groupMembers) {
            if (gm.getRole() == Role.ADMIN) {
                return true;
            }
        }

        return false;
    }

    public void makeNewAdmin(List<GroupMember> groupMembers) {
        GroupMember groupMember = groupMembers.get(0);
        groupMember.upgradeToAdmin();
    }
}
