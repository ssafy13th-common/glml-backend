package com.ssafy.a705.domain.board._post.service;

import com.ssafy.a705.domain.board._reply.entity.Reply;
import com.ssafy.a705.domain.board._reply.repository.CompanyCommentRepository;
import com.ssafy.a705.domain.board._post.dto.request.PostDetailReq;
import com.ssafy.a705.domain.board._post.dto.response.PostCreateRes;
import com.ssafy.a705.domain.board._post.dto.response.PostDetailRes;
import com.ssafy.a705.domain.board._post.dto.response.PostInfosRes;
import com.ssafy.a705.domain.board._post.entity.Post;
import com.ssafy.a705.domain.board._post.exception.PostNotFoundException;
import com.ssafy.a705.domain.board._post.exception.DeletedPostException;
import com.ssafy.a705.domain.board._post.repository.PostRepository;
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
public class PostService {

    private final S3PresignedUploader uploader;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CompanyCommentRepository commentRepository;

    @Transactional
    public PostCreateRes createPost(PostDetailReq postReq, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = Post.from(postReq, member);
        postRepository.save(post);
        return PostCreateRes.from(post);
    }

    @Transactional(readOnly = true)
    public PostInfosRes getPosts(Long cursorId) {
        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "id"));
        List<Post> posts = postRepository.findAllNotDeleted(cursorId, pageable);

        Long nextCursor = posts.isEmpty() ? 1 : posts.get(posts.size() - 1).getId();
        return PostInfosRes.from(posts, nextCursor);
    }

    @Transactional(readOnly = true)
    public PostDetailRes getPost(Long postId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = getPostById(postId);
        List<Reply> comments = commentRepository.findAllByCompanyBoardAndNotDeleted(post);

        Map<Long, String> urls = new HashMap<>();
        String url = getUrl(post.getMember().getProfileUrl());
        urls.put(post.getMember().getId(), url);

        for (Reply comment : comments) {
            if (urls.containsKey(comment.getMember().getId())) {
                continue;
            }

            url = getUrl(comment.getMember().getProfileUrl());
            urls.put(comment.getMember().getId(), url);
        }

        return PostDetailRes.from(post, comments, urls);
    }

    @Transactional
    public void updatePost(Long postId, PostDetailReq postReq, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = getPostById(postId);
        checkMemberCanEdit(member, post);
        post.update(postReq);
    }

    @Transactional
    public void deletePost(Long postId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = getPostById(postId);
        checkMemberCanEdit(member, post);
        post.deletePost();
    }

    public Page<Post> getMemberPost(Member member, Pageable pageable) {
        return postRepository.findAllByMemberNotDeleted(member, pageable);
    }

    private void checkMemberCanEdit(Member member, Post post) {
        if (!Objects.equals(member, post.getMember())) {
            throw new ForbiddenException("게시물 접근");
        }
    }

    public Post getPostById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new PostNotFoundException();
        }

        Post post = postOptional.get();
        if (Objects.isNull(post.getDeletedAt())) {
            return post;
        }

        throw new DeletedPostException();
    }

    private String getUrl(String url) {
        if (url.startsWith("members/")) {
            return uploader.generatePresignedGetUrl(url);
        }
        return url;
    }

}