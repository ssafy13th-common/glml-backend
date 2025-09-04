package com.ssafy.a705.global.common.controller;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ApiResponse<T>(
        String message,
        T data
) {

    public static <T> ApiResponse<T> of() {
        return new ApiResponse<>(null, null);
    }

    public static <T> ApiResponse<T> failedOf(String message) {
        return new ApiResponse<>(message, null);
    }


    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResponse<>(null, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(null, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status, T data, String message) {
        return ResponseEntity.status(status).body(new ApiResponse<>(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> failedOf(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiResponse<>(message, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> failedOf(ApiException e) {
        return ResponseEntity.status(e.status).body(new ApiResponse<>(e.getMessage(), null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok() {
        return ApiResponse.of(HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ApiResponse.of(HttpStatus.OK, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> okMessage(String message) {
        return ApiResponse.of(HttpStatus.OK, null, message);
    }

    public static <T> ResponseEntity<ApiResponse<T>> create() {
        return ApiResponse.of(HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<ApiResponse<T>> create(T data) {
        return ApiResponse.of(HttpStatus.CREATED, data);
    }


}
