package com.project.logistic_management_2.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class BaseResponse<T> {
    private boolean success;
    private T data;
    private String message;

    public static <T> BaseResponse<T> ok(T data) {
        return BaseResponse.<T>builder()
                .data(data)
                .success(true)
                .message("Thành công")
                .build();
    }

    public static <T> BaseResponse<T> ok(T data, String message) {
        return BaseResponse.<T>builder()
                .data(data)
                .success(true)
                .message(message)
                .build();
    }

    public static <T> BaseResponse<T> fail(String msg) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(msg)
                .build();
    }
}
