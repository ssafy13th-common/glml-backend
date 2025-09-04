package com.ssafy.a705.global.common.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ApiResponse<Void>> handleError() {
        return ApiResponse.failedOf(HttpStatus.NOT_FOUND, "요청한 페이지를 찾을 수 없습니다.");
    }

}
