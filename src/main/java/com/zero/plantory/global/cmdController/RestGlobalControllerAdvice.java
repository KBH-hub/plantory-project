package com.zero.plantory.global.cmdController;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestGlobalControllerAdvice {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleNotReadable(HttpMessageNotReadableException ex,
                                                                 HttpServletRequest req) {
        return body(HttpStatus.BAD_REQUEST, "REQ-400-JSON",
                "요청 본문 파싱 실패", safeMsg(ex), req.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalid(MethodArgumentNotValidException ex,
                                                             HttpServletRequest req) {
        String merged = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining(", "));
        return body(HttpStatus.BAD_REQUEST, "REQ-400-VALID",
                "검증 실패", merged, req.getRequestURI());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParam(MissingServletRequestParameterException ex,
                                                                  HttpServletRequest req) {
        return body(HttpStatus.BAD_REQUEST, "REQ-400-MISSING",
                "필수 파라미터 누락", ex.getParameterName() + " 파라미터가 필요합니다.", req.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                  HttpServletRequest req) {
        String msg = "파라미터 '" + ex.getName() + "' 타입 불일치";
        return body(HttpStatus.BAD_REQUEST, "REQ-400-TYPE", "타입 불일치", msg, req.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex,
                                                                  HttpServletRequest req) {
        return body(HttpStatus.FORBIDDEN, "SEC-403",
                "접근 거부", "이 리소스에 접근 권한이 없습니다.", req.getRequestURI());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException ex,
                                                              HttpServletRequest req) {
        return body(HttpStatus.NOT_FOUND, "RES-404",
                "리소스를 찾을 수 없음", safeMsg(ex), req.getRequestURI());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                        HttpServletRequest req) {
        return body(HttpStatus.METHOD_NOT_ALLOWED, "REQ-405",
                "메서드 허용 안 됨", safeMsg(ex), req.getRequestURI());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                           HttpServletRequest req) {
        return body(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "REQ-415",
                "미지원 미디어 타입", safeMsg(ex), req.getRequestURI());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleConflict(DataIntegrityViolationException ex,
                                                              HttpServletRequest req) {
        return body(HttpStatus.CONFLICT, "DB-409",
                "데이터 충돌", "무결성 제약을 위반했습니다.", req.getRequestURI());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex,
                                                             HttpServletRequest req) {
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "SYS-500-RUNTIME",
                "서버 오류", "예기치 못한 오류가 발생했습니다.", req.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAny(Exception ex,
                                                         HttpServletRequest req) {
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "SYS-500",
                "서버 오류", "예기치 못한 오류가 발생했습니다.", req.getRequestURI());
    }

    private String formatFieldError(FieldError f) {
        String field = f.getField();
        String msg = f.getDefaultMessage();
        return field + ": " + (msg == null ? "유효하지 않습니다." : msg);
    }

    private ResponseEntity<Map<String, String>> body(HttpStatus status,
                                                     String code,
                                                     String title,
                                                     String message,
                                                     String path) {
        return ResponseEntity.status(status).body(Map.of(
                "code", code,
                "title", title,
                "message", nullToEmpty(message),
                "path", nullToEmpty(path),
                "status", String.valueOf(status.value())
        ));
    }

    private String safeMsg(Throwable t) {
        String m = t.getMessage();
        return (m == null || m.isBlank()) ? t.getClass().getSimpleName() : m;
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
