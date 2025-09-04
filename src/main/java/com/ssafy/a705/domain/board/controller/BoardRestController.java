package com.ssafy.a705.domain.board.controller;

import com.ssafy.a705.domain.board.dto.request.BoardDetailReq;
import com.ssafy.a705.domain.board.dto.response.BoardCreateRes;
import com.ssafy.a705.domain.board.dto.response.BoardDetailRes;
import com.ssafy.a705.domain.board.dto.response.BoardInfosRes;
import com.ssafy.a705.domain.board.service.BoardService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardRestController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<ApiResponse<BoardCreateRes>> createBoard(
            @RequestBody @Valid BoardDetailReq boardCreateReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BoardCreateRes res = boardService.createBoard(boardCreateReq, userDetails);
        return ApiResponse.create(res);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<BoardInfosRes>> getBoards(
            @RequestParam(required = false) Long cursorId
    ) {
        BoardInfosRes res = boardService.getBoards(cursorId);
        return ApiResponse.ok(res);
    }

    @GetMapping("/{board-id}")
    public ResponseEntity<ApiResponse<BoardDetailRes>> getBoard(
            @PathVariable("board-id") Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BoardDetailRes res = boardService.getBoard(boardId, userDetails);
        return ApiResponse.ok(res);
    }

    @PutMapping("/{board-id}")
    public ResponseEntity<ApiResponse<Void>> updateBoard(
            @PathVariable("board-id") Long boardId,
            @RequestBody @Valid BoardDetailReq boardReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        boardService.updateBoard(boardId, boardReq, userDetails);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{board-id}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @PathVariable("board-id") Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        boardService.deleteBoard(boardId, userDetails);
        return ApiResponse.ok();
    }


}