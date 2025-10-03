package com.ssafy.a705.domain.board.service;

import com.ssafy.a705.domain.board._reply.entity.Reply;
import com.ssafy.a705.domain.board._reply.repository.CompanyCommentRepository;
import com.ssafy.a705.domain.board.dto.request.BoardDetailReq;
import com.ssafy.a705.domain.board.dto.response.BoardCreateRes;
import com.ssafy.a705.domain.board.dto.response.BoardDetailRes;
import com.ssafy.a705.domain.board.dto.response.BoardInfosRes;
import com.ssafy.a705.domain.board.entity.Post;
import com.ssafy.a705.domain.board.exception.BoardNotFoundException;
import com.ssafy.a705.domain.board.exception.DeletedBoardException;
import com.ssafy.a705.domain.board.repository.CompanyBoardRepository;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.common.utils.S3PresignedUploader;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final S3PresignedUploader uploader;
    private final MemberRepository memberRepository;
    private final CompanyBoardRepository boardRepository;
    private final CompanyCommentRepository commentRepository;

    @Transactional
    public BoardCreateRes createBoard(BoardDetailReq boardReq, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post board = Post.from(boardReq, member);
        boardRepository.save(board);
        return BoardCreateRes.from(board);
    }

    @Transactional(readOnly = true)
    public BoardInfosRes getBoards(Long cursorId) {
        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "id"));
        List<Post> boards = boardRepository.findAllNotDeleted(cursorId, pageable);

        Long nextCursor = boards.isEmpty() ? 1 : boards.get(boards.size() - 1).getId();
        return BoardInfosRes.from(boards, nextCursor);
    }

    @Transactional(readOnly = true)
    public BoardDetailRes getBoard(Long boardId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post board = getBoardById(boardId);
        List<Reply> comments = commentRepository.findAllByCompanyBoardAndNotDeleted(board);

        Map<Long, String> urls = new HashMap<>();
        String url = getUrl(board.getMember().getProfileUrl());
        urls.put(board.getMember().getId(), url);

        for (Reply comment : comments) {
            if (urls.containsKey(comment.getMember().getId())) {
                continue;
            }

            url = getUrl(comment.getMember().getProfileUrl());
            urls.put(comment.getMember().getId(), url);
        }

        return BoardDetailRes.from(board, comments, urls);
    }

    @Transactional
    public void updateBoard(Long boardId, BoardDetailReq boardReq, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post board = getBoardById(boardId);
        checkMemberCanEdit(member, board);
        board.update(boardReq);
    }

    @Transactional
    public void deleteBoard(Long boardId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post board = getBoardById(boardId);
        checkMemberCanEdit(member, board);
        board.deletePost();
    }

    public Page<Post> getMemberBoard(Member member, Pageable pageable) {
        return boardRepository.findAllByMemberNotDeleted(member, pageable);
    }

    private void checkMemberCanEdit(Member member, Post board) {
        if (!Objects.equals(member, board.getMember())) {
            throw new ForbiddenException("게시물 접근");
        }
    }

    public Post getBoardById(Long boardId) {
        Optional<Post> board = boardRepository.findById(boardId);
        if (board.isEmpty()) {
            throw new BoardNotFoundException();
        }

        Post post = board.get();
        if (Objects.isNull(post.getDeletedAt())) {
            return post;
        }

        throw new DeletedBoardException();
    }

    private String getUrl(String url) {
        if (url.startsWith("members/")) {
            return uploader.generatePresignedGetUrl(url);
        }
        return url;
    }

}