package com.ssafy.a705.board.application;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.board.domain.service.PostService;
import com.ssafy.a705.board.domain.service.PostStore;
import com.ssafy.a705.board.infrastructure.repository.ReplyJpaRepository;
import com.ssafy.a705.board.presentation.dto.request.PostDetailReq;
import com.ssafy.a705.board.presentation.dto.response.PostCreateRes;
import com.ssafy.a705.board.presentation.dto.response.PostDetailRes;
import com.ssafy.a705.board.presentation.dto.response.PostInfosRes;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostApplicationService {

    private final MemberRepository memberRepository;
    private final PostService postService;
    private final PostStore postStore;
    private final ReplyJpaRepository replyRepository;

    @Transactional
    public PostCreateRes createPost(PostDetailReq postReq, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = Post.from(postReq, member);
        postStore.savePost(post);
        return PostCreateRes.from(post);
    }

    @Transactional(readOnly = true)
    public PostInfosRes getPosts(Long cursorId) {
        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "id"));
        List<Post> posts = postService.getPosts(cursorId, pageable);
        Long nextCursor = posts.isEmpty() ? 1 : posts.get(posts.size() - 1).getId();
        return PostInfosRes.from(posts, nextCursor);
    }

    @Transactional(readOnly = true)
    public PostDetailRes getPost(Long postId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = postService.getPostById(postId);
        List<Reply> replies = replyRepository.findAllByPostAndNotDeleted(post);

        Map<Long, String> urls = new HashMap<>();
        String url = postService.getUrl(post.getMember().getProfileUrl());
        urls.put(post.getMember().getId(), url);

        for (Reply reply : replies) {
            if (urls.containsKey(reply.getMember().getId())) {
                continue;
            }

            url = postService.getUrl(reply.getMember().getProfileUrl());
            urls.put(reply.getMember().getId(), url);
        }

        return PostDetailRes.from(post, replies, urls);
    }

    @Transactional
    public void updatePost(Long postId, PostDetailReq postReq, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = postService.getPostById(postId);
        postService.checkMemberCanEdit(member, post);
        post.update(postReq);
    }

    @Transactional
    public void deletePost(Long postId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = postService.getPostById(postId);
        postService.checkMemberCanEdit(member, post);
        post.deletePost();
    }

}