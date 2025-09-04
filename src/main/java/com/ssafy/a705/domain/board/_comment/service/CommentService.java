package com.ssafy.a705.domain.board._comment.service;

import com.ssafy.a705.domain.board._comment.dto.request.CommentRegisterReq;
import com.ssafy.a705.domain.board._comment.dto.request.CommentUpdateReq;
import com.ssafy.a705.domain.board._comment.entity.CompanyComment;
import com.ssafy.a705.domain.board._comment.repository.CompanyCommentRepository;
import com.ssafy.a705.domain.board.entity.CompanyBoard;
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
        CompanyBoard board = boardService.getBoardById(boardId);
        CompanyComment parent = commentRepository.findByIdAndCompanyBoard(commentReq.parentId(),
                board).orElse(null);
        CompanyComment comment = CompanyComment.from(commentReq, board, member, parent);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long boardId, Long commentId, CommentUpdateReq commentUpdateReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        CompanyBoard board = boardService.getBoardById(boardId);
        CompanyComment comment = commentRepository.getByIdAndCompanyBoard(commentId, board);
        checkMemberCanEdit(member, comment);
        comment.updateComment(commentUpdateReq);
    }

    @Transactional
    public void deleteComment(Long boardId, Long commentId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        CompanyBoard board = boardService.getBoardById(boardId);
        CompanyComment comment = commentRepository.getByIdAndCompanyBoard(commentId, board);
        checkMemberCanEdit(member, comment);
        comment.deleteComment();
    }

    public Page<CompanyComment> getMemberComments(Member member, Pageable pageable) {
        return commentRepository.findAllByMemberNotDeleted(member, pageable);
    }

    private void checkMemberCanEdit(Member member, CompanyComment comment) {
        if (!Objects.equals(member, comment.getMember())) {
            throw new ForbiddenException("댓글 접근");
        }
    }

}