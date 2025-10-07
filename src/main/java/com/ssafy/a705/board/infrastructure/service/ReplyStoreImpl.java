package com.ssafy.a705.board.infrastructure.service;

import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.board.domain.repository.ReplyRepository;
import com.ssafy.a705.board.domain.service.ReplyStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplyStoreImpl implements ReplyStore {

    private final ReplyRepository replyRepository;

    @Override
    public void saveReply(Reply reply) {
        replyRepository.save(reply);
    }
}
