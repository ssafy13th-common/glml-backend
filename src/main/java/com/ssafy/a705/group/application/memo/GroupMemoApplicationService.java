package com.ssafy.a705.group.application.memo;


import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.application.participant.ParticipantApplicationService;
import com.ssafy.a705.group.domain.memo.entity.GroupMemo;
import com.ssafy.a705.group.domain.memo.exception.MemoAccessDeniedException;
import com.ssafy.a705.group.domain.memo.service.GroupMemoReader;
import com.ssafy.a705.group.domain.memo.service.GroupMemoStore;
import com.ssafy.a705.group.domain.participant.entity.Participant;
import com.ssafy.a705.group.presentation.memo.dto.request.GroupMemoReq;
import com.ssafy.a705.group.presentation.memo.dto.response.GroupMemoRes;
import com.ssafy.a705.group.presentation.memo.dto.response.GroupMemosRes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupMemoApplicationService {

    private final ParticipantApplicationService participantService;
    private final GroupMemoStore groupMemoStore;
    private final GroupMemoReader groupMemoReader;

    @Transactional
    public GroupMemoRes createMemo(Long groupId, GroupMemoReq groupMemoReq,
                                   CustomUserDetails customUserDetails) {
        Participant currentMember = participantService.memberAuthorityCheck(groupId,
                customUserDetails);

        GroupMemo memo = GroupMemo.of(groupMemoReq.content(), currentMember);
        groupMemoStore.saveGroupMemo(memo);
        return GroupMemoRes.from(memo);
    }

    @Transactional(readOnly = true)
    public GroupMemosRes getMemos(Long groupId, CustomUserDetails customUserDetails) {
        participantService.memberAuthorityCheck(groupId, customUserDetails);

        List<GroupMemo> memos = groupMemoReader.getAllByGroupId(groupId);
        return GroupMemosRes.of(groupId, memos);
    }

    @Transactional
    public GroupMemoRes updateMemo(Long groupId, Long memoId, GroupMemoReq groupMemoReq,
            CustomUserDetails customUserDetails) {
        GroupMemo memo = memoAuthorityCheck(groupId, memoId, customUserDetails);

        memo.updateMemo(groupMemoReq.content());
        return GroupMemoRes.from(memo);
    }

    @Transactional
    public void deleteMemo(Long groupId, Long memoId, CustomUserDetails customUserDetails) {
        GroupMemo memo = memoAuthorityCheck(groupId, memoId, customUserDetails);
        memo.deleteMemo();
    }

    private GroupMemo memoAuthorityCheck(Long groupId, Long memoId,
            CustomUserDetails customUserDetails) {
        // 로그인 된 유저가 그룹 멤버인지 체크
        Participant currentMember = participantService.memberAuthorityCheck(groupId,
                customUserDetails);

        GroupMemo memo = groupMemoReader.getById(memoId);

        // 현재 로그인된 유저가 메모 작성자인지 체크
        if (memo.getParticipant().getId() != currentMember.getId()) {
            throw new MemoAccessDeniedException();
        }
        return memo;
    }
}
