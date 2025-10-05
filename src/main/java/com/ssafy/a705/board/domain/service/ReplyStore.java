package com.ssafy.a705.board.domain.service;

import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.board.infrastructure.repository.ReplyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyStore {

    private final ReplyJpaRepository replyJpaRepository;

    public void saveReply(Reply reply) {
        replyJpaRepository.save(reply);
    }

}
