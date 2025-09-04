package com.ssafy.a705.domain.group.service;

import com.ssafy.a705.domain.group.dto.request.GroupReq;
import com.ssafy.a705.domain.group.dto.request.GroupUpdateReq;
import com.ssafy.a705.domain.group.entity.Group;
import com.ssafy.a705.domain.group.repository.GroupRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    /**
     * 그룹 생성 (그룹 생성 후 현재 로그인 된 유저 정보를 통해 GroupMember 생성)
     *
     * @param groupReq 그룹 생성 Request
     * @return 생성된 그룹
     */
    @Transactional
    public Group createGroup(GroupReq groupReq) {

        // 그룹 생성 후 DB에 저장
        Group group = Group.from(groupReq);

        return groupRepository.save(group);
    }

    /**
     * 그룹 상세 정보 조회
     *
     * @param groupId 그룹 식별자
     * @return 그룹
     */
    @Transactional(readOnly = true)
    public Group getGroup(Long groupId) {
        Group group = groupRepository.getById(groupId);
        return group;
    }

    /**
     * 그룹 목록 조회
     *
     * @param customUserDetails
     * @return
     */
    @Transactional(readOnly = true)
    public List<Group> getGroups(CustomUserDetails customUserDetails) {
        List<Group> groups = groupRepository.findGroupsByMemberId(customUserDetails.getId());
        return groups;
    }

    /**
     * 그룹 정보 업데이트
     *
     * @param groupId
     * @param groupUpdateReq
     * @return
     */
    @Transactional
    public Group updateGroup(Long groupId, GroupUpdateReq groupUpdateReq) {
        Group group = groupRepository.getById(groupId);
        group.update(groupUpdateReq);
        return groupRepository.save(group);
    }

    /**
     * 그룹 삭제 처리
     *
     * @param groupId
     */
    @Transactional
    public void deleteGroup(Long groupId) {
        Group group = groupRepository.getById(groupId);
        group.deleteGroup();
    }

    public void saveGroup(Group group) {
        groupRepository.save(group);
    }
}
