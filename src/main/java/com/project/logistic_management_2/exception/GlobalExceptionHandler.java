package com.project.logistic_management_2.exception;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.exception.def.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.rmi.ServerException;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                BaseResponse.fail(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException (NotFoundException ex) {
        return new ResponseEntity<>(
                BaseResponse.fail(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException ex) {
        return new ResponseEntity<>(
                BaseResponse.fail(ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(BaseResponse.fail("Sai tên đăng nhập hoặc mật khẩu."), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<BaseResponse<String>> handleForbiddenException(ForbiddenException ex) {
        return new ResponseEntity<>(BaseResponse.fail(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(
                BaseResponse.fail(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EditNotAllowedException.class)
    public ResponseEntity<Object> handleEditNotAllowedException(EditNotAllowedException ex) {
        return new ResponseEntity<>(
                BaseResponse.fail(ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Object> handleInvalidParameterException(InvalidParameterException ex) {
        return new ResponseEntity<>(
                BaseResponse.fail(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String errorMessage = "Phương thức HTTP không được hỗ trợ: " + ex.getMethod();
        return new ResponseEntity<>(
                BaseResponse.fail(errorMessage),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Object> handleServerException(ServerException ex) {
        return new ResponseEntity<>(
                BaseResponse.fail("Đã có lỗi xảy ra. Vui lòng thử lại sau!"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
