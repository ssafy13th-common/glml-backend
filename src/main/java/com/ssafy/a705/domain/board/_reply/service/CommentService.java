package com.ssafy.a705.domain.board._reply.service;

import com.ssafy.a705.domain.board._reply.dto.request.CommentRegisterReq;
import com.ssafy.a705.domain.board._reply.dto.request.CommentUpdateReq;
import com.ssafy.a705.domain.board._reply.entity.Reply;
import com.ssafy.a705.domain.board._reply.repository.CompanyCommentRepository;
import com.ssafy.a705.domain.board.entity.Post;
import com.ssafy.a705.domain.board.service.BoardService;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final BoardService boardService;
    private final CompanyCommentRepository commentRepository;

    @Transactional
    public void createComment(Long boardId, CommentRegisterReq commentReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post board = boardService.getBoardById(boardId);
        Reply parent = commentRepository.findByIdAndCompanyBoard(commentReq.parentId(),
                board).orElse(null);
        Reply comment = Reply.from(commentReq, board, member, parent);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long boardId, Long commentId, CommentUpdateReq commentUpdateReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post board = boardService.getBoardById(boardId);
        Reply comment = commentRepository.getByIdAndCompanyBoard(commentId, board);
        checkMemberCanEdit(member, comment);
        comment.updateContent(commentUpdateReq);
    }

    @Transactional
    public void deleteComment(Long boardId, Long commentId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post board = boardService.getBoardById(boardId);
        Reply comment = commentRepository.getByIdAndCompanyBoard(commentId, board);
        checkMemberCanEdit(member, comment);
        comment.deleteReply();
    }

    public Page<Reply> getMemberComments(Member member, Pageable pageable) {
        return commentRepository.findAllByMemberNotDeleted(member, pageable);
    }

    private void checkMemberCanEdit(Member member, Reply comment) {
        if (!Objects.equals(member, comment.getMember())) {
            throw new ForbiddenException("댓글 접근");
        }
    }

}