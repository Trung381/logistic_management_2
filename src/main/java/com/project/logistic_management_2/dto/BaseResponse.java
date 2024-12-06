package com.project.logistic_management_2.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class BaseResponse<T> {
    private String status;
    private T data;
    private String message;

    public static <T> BaseResponse<T> ok(T data) {
        return BaseResponse.<T>builder()
                .data(data)
                .status("success")
                .message("hehe trung đặt message ni cho case success đấy 😼(hiện alert message này nhen)")
                .build();
    }

    public static <T> BaseResponse<T> fail(String msg) {
        return BaseResponse.<T>builder()
                .status("fail")
                .message(msg)
                .build();
    }
}
