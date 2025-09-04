package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.board.entity.CompanyBoard;
import java.util.List;
import org.springframework.data.domain.Page;

public record MemberBoardsRes(
        List<MemberBoard> boards,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    public static MemberBoardsRes from(Page<CompanyBoard> boards) {
        return new MemberBoardsRes(boards.stream()
                .map(MemberBoard::from).toList(),
                boards.getNumber(),
                boards.getSize(),
                boards.getTotalElements(),
                boards.getTotalPages()
        );
    }

}
