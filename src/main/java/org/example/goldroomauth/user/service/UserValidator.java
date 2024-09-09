package org.example.goldroomauth.user.service;

import lombok.RequiredArgsConstructor;
import org.example.goldroomauth.exception.BadRequestException;
import org.example.goldroomauth.exception.ConflictException;
import org.example.goldroomauth.exception.ErrorCode;
import org.example.goldroomauth.user.dto.SignUpRequest;
import org.example.goldroomauth.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    // 회원 정보 검사
    public void validateUserInfo(SignUpRequest signUpRequest) {
        // 1. null 검사
        if (signUpRequest == null){
            throw new BadRequestException(ErrorCode.INVALID_USER_REQUEST_BODY);
        }
        // 2. username 형식 검사
        validateUsername(signUpRequest.username());
        // 3. username 중복 검사
        if (userRepository.findByUsername(signUpRequest.username()).isPresent()){
            throw new ConflictException(ErrorCode.PRESENT_USER_NAME_CONFLICT);
        }
        // 4. password 형식 검사
        validatePassword(signUpRequest.password(), signUpRequest.username(), signUpRequest.phoneNumber());
        // 5. cellphone 형식 검사 (010 휴대폰번호만 허용)
        if (!signUpRequest.phoneNumber().matches("^(010)[-]?\\d{4}[-]?\\d{4}$")) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_USER_CELLPHONE_FORMAT);
        }
    }

    // username 형식 검사
    private void validateUsername(String username){
        // 1. 8자리 미만일 경우
        if(username.length()<8){
            throw new BadRequestException(ErrorCode.BAD_REQUEST_USER_MIN_NAME_LENGTH);
        }
        // 2. 숫자와 영어소문자 구성 검사
        if (!username.matches("^[a-z0-9]+$")){
            throw new BadRequestException(ErrorCode.BAD_REQUEST_USER_NAME_FORMAT);
        }
        // 3. 숫자로만 이루어진 경우
        if (username.chars().allMatch(Character::isDigit)){
            throw new BadRequestException(ErrorCode.BAD_REQUEST_SIGN_UP_DIG_FORMAT);
        }
    }

    // password 형식 검사
    private void validatePassword(String password,String username,String phoneNumber){
        // 1. 10자리 미만일 경우
        if (password.length()<10){
            throw new BadRequestException(ErrorCode.BAD_REQUEST_USER_MIN_PASSWORD_LENGTH);
        }
        // 2. 숫자로만 이루어진 경우
        if (password.chars().allMatch(Character::isDigit)){
            throw new BadRequestException(ErrorCode.BAD_REQUEST_SIGN_UP_DIG_FORMAT);
        }
        // 3. 숫자, 문자, 특수문자 중 최소 2가지 이상 포함하지 않은 경우
        if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9!@#$%^&*].*")) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_USER_PASSWORD_FORMAT);
        }
        // 4. 연속된 문자열을 사용한 경우
        if (hasSequentialCharacters(password)){
            throw new BadRequestException(ErrorCode.BAD_REQUEST_USER_PASSWORD_SEQ);
        }
        // 5. 사용자 개인정보를 포함한 경우
        if (isSimilarToPersonalInfo(password,username,phoneNumber)){
            throw new BadRequestException(ErrorCode.BAD_REQUEST_USER_PASSWORD_INFO);
        }
    }
    // 3회 이상 연속된 문자열을 사용한 비밀번호 검사
    private boolean hasSequentialCharacters(String password) {
        if (password == null || password.length() < 3) {
            return false;
        }
        int seqCount = 1;
        for (int i = 1; i < password.length(); i++) {
            // 현재 문자와 이전 문자의 차이를 비교
            if (password.charAt(i) == password.charAt(i - 1) + 1) {
                seqCount++;
                if (seqCount >= 3) {
                    return true;
                }
            } else {
                seqCount = 1;
            }
        }
        return false;
    }
    // 사용자 개인정보를 포함한 경우
    private boolean isSimilarToPersonalInfo(String password, String username, String phoneNumber) {
        return password.toLowerCase().contains(username) || password.toLowerCase().contains(phoneNumber);
    }

}
