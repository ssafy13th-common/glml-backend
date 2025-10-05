package com.ssafy.a705.board.domain.service;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.exception.DeletedPostException;
import com.ssafy.a705.board.domain.exception.PostNotFoundException;
import com.ssafy.a705.board.domain.repository.PostRepository;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.common.utils.S3PresignedUploader;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final S3PresignedUploader uploader;
    private final PostRepository postRepository;


    public Page<Post> getMemberPost(Member member, Pageable pageable) {
        return postRepository.findAllByMemberNotDeleted(member, pageable);
    }

    public void checkMemberCanEdit(Member member, Post post) {
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

    public String getUrl(String url) {
        if (url.startsWith("members/")) {
            return uploader.generatePresignedGetUrl(url);
        }
        return url;
    }

    public List<Post> getPosts(Long cursorId, Pageable pageable) {
        return postRepository.findAllNotDeleted(cursorId, pageable);
    }
}
