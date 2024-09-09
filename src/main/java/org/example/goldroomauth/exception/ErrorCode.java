package org.example.goldroomauth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    // 클라이언트의 입력 값에 대한 일반적인 오류 (@PathVariable, @RequestParam가 잘못되었을 때)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "클라이언트의 입력 값을 확인해주세요."),
    // @RequestBody의 입력 값이 유효하지 않을 때
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터 값을 확인해주세요."),
    // 요청 본문 형식 오류 추가
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 본문의 형식이 올바르지 않습니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 엔티티입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서비스 이용에 장애가 있습니다."),

    // 회원가입
    INVALID_USER_REQUEST_BODY(HttpStatus.BAD_REQUEST,"회원정보가 전송되지 않았습니다."),
    BAD_REQUEST_USER_MIN_NAME_LENGTH(HttpStatus.BAD_REQUEST,"username은 8자리 이상으로 설정해주세요!"),
    BAD_REQUEST_USER_NAME_FORMAT(HttpStatus.BAD_REQUEST,"username은 영어소문자와 숫자로만 구성되어야 합니다."),
    BAD_REQUEST_SIGN_UP_DIG_FORMAT(HttpStatus.BAD_REQUEST,"숫자로만 구성되어 있습니다."),
    BAD_REQUEST_USER_MIN_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST,"password는 10자리 이상으로 설정해주세요!"),
    BAD_REQUEST_USER_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST,"password는 영어, 숫자, 특수문자 중 최소 2가지를 포함해야 합니다."),
    BAD_REQUEST_USER_PASSWORD_SEQ(HttpStatus.BAD_REQUEST,"3회 이상 연속되는 숫자, 문자열을 포함하고 있습니다."),
    BAD_REQUEST_USER_PASSWORD_INFO(HttpStatus.BAD_REQUEST,"개인정보가 포함된 비밀번호입니다."),
    BAD_REQUEST_USER_CELLPHONE_FORMAT(HttpStatus.BAD_REQUEST,"휴대전화 형식이 올바르지 않습니다."),
    PRESENT_USER_NAME_CONFLICT(HttpStatus.CONFLICT,"이미 존재하는 username입니다."),

    // 로그인 및 JWT
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"올바르지 않은 비밀번호입니다."),
    REFRESH_TOKEN_EXPIRATION(HttpStatus.UNAUTHORIZED,"인증정보가 만료됐습니다. 다시 로그인해주세요."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"유효하지 않은 리프레시 토큰입니다.");
    //공통
    private final HttpStatus status;
    private final String message;
}
