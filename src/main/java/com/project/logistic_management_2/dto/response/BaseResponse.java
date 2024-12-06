package com.project.logistic_management_2.dto.response;

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
                .build();
    }

    public static <T> BaseResponse<T> fail(String msg) {
        return BaseResponse.<T>builder()
                .status("fail")
                .message(msg)
                .build();
    }
}
