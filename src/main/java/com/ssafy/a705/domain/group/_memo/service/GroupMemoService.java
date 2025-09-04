package com.ssafy.a705.domain.group._memo.service;

import com.ssafy.a705.domain.group._member.entity.GroupMember;
import com.ssafy.a705.domain.group._member.service.GroupMemberService;
import com.ssafy.a705.domain.group._memo.dto.request.GroupMemoReq;
import com.ssafy.a705.domain.group._memo.dto.response.GroupMemoRes;
import com.ssafy.a705.domain.group._memo.dto.response.GroupMemosRes;
import com.ssafy.a705.domain.group._memo.entity.GroupMemo;
import com.ssafy.a705.domain.group._memo.exception.MemoAccessDeniedException;
import com.ssafy.a705.domain.group._memo.repository.GroupMemoRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupMemoService {

    private final GroupMemoRepository groupMemoRepository;
    private final GroupMemberService groupMemberService;

    // 메모 생성
    @Transactional
    public GroupMemoRes createMemo(Long groupId, GroupMemoReq groupMemoReq,
            CustomUserDetails customUserDetails) {
        GroupMember currentMember = groupMemberService.memberAuthorityCheck(groupId,
                customUserDetails);

        GroupMemo newMemo = GroupMemo.of(groupMemoReq.content(), currentMember);
        groupMemoRepository.save(newMemo);
        return GroupMemoRes.from(newMemo);
    }

    // 메모 전체 조회
    @Transactional(readOnly = true)
    public GroupMemosRes getMemos(Long groupId, CustomUserDetails customUserDetails) {
        groupMemberService.memberAuthorityCheck(groupId, customUserDetails);

        List<GroupMemo> memos = groupMemoRepository.findAllByGroupIdAndDeletedAtIsNull(groupId);
        return GroupMemosRes.of(groupId, memos);
    }

    // 메모 수정
    @Transactional
    public GroupMemoRes updateMemo(Long groupId, Long memoId, GroupMemoReq groupMemoReq,
            CustomUserDetails customUserDetails) {
        GroupMemo memo = memoAuthorityCheck(groupId, memoId, customUserDetails);

        memo.updateMemo(groupMemoReq.content());
        return GroupMemoRes.from(memo);
    }

    // 메모 삭제
    @Transactional
    public void deleteMemo(Long groupId, Long memoId, CustomUserDetails customUserDetails) {
        GroupMemo memo = memoAuthorityCheck(groupId, memoId, customUserDetails);
        memo.deleteMemo();
    }

    private GroupMemo memoAuthorityCheck(Long groupId, Long memoId,
            CustomUserDetails customUserDetails) {
        // 로그인 된 유저가 그룹 멤버인지 체크
        GroupMember currentMember = groupMemberService.memberAuthorityCheck(groupId,
                customUserDetails);

        GroupMemo memo = groupMemoRepository.getGroupMemoById(memoId);

        // 현재 로그인된 유저가 메모 작성자인지 체크
        if (memo.getGroupMember().getId() != currentMember.getId()) {
            throw new MemoAccessDeniedException();
        }
        return memo;
    }
}
