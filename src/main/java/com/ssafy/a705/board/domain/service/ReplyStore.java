package com.ssafy.a705.board.domain.service;

import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.board.domain.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyStore {

    private final ReplyRepository replyRepository;

    public void saveReply(Reply reply) {
        replyRepository.save(reply);
    }

}
