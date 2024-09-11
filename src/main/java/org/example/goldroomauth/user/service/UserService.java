package org.example.goldroomauth.user.service;

import lombok.RequiredArgsConstructor;
import org.example.goldroomauth.config.JwtTokenProvider;
import org.example.goldroomauth.exception.BadRequestException;
import org.example.goldroomauth.exception.ErrorCode;
import org.example.goldroomauth.exception.NotFoundException;
import org.example.goldroomauth.user.domain.Token;
import org.example.goldroomauth.user.domain.User;
import org.example.goldroomauth.user.dto.*;
import org.example.goldroomauth.user.repository.TokenRepository;
import org.example.goldroomauth.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    // 회원가입
    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        // 1. 사용자의 입력내용 유효성 검사
        userValidator.validateUserInfo(signUpRequest);
        // 2. 휴대전화 (하이픈 처리)
        String processedPhone = formatCellPhoneNumber(signUpRequest.phoneNumber());
        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequest.password());
        // 3. User 생성
        User user = User.builder()
                .username(signUpRequest.username())
                .password(encodedPassword)
                .nickname(signUpRequest.nickname())
                .phoneNumber(processedPhone)
                .zipCode(signUpRequest.zipCode())
                .address(signUpRequest.address())
                .addressDetail(signUpRequest.addressDetail())
                .build();
        // 4. 사용자 정보 DB에 저장
        userRepository.save(user);
        // 5.SignUpResponse 생성 및 반환
        return new SignUpResponse("회원가입이 완료되었습니다.", user.getUsername(), user.getNickname());
    }

    // 로그인
    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) {
        // 1. username으로 사용자 정보 확인하기
        User user = userRepository.findByUsername(signInRequest.username())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        // 2. password 확인하기
        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())){
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD);
        }
        // 3. refreshToken 확인하기
        Optional<Token> refreshToken = tokenRepository.findByUser_UserId(user.getUserId());
        // 4. 새로운 refreshToken 발급
        String newRefreshToken = jwtTokenProvider.generateToken(signInRequest.username(),"refresh");
        if (refreshToken.isPresent()) { // 리프레시 토큰 있을 경우
            refreshToken.get().updateToken(newRefreshToken); // 새 토큰으로 업데이트
        } else { // 리프레시 토큰 없을 경우
            tokenRepository.save(new Token(newRefreshToken, user)); // 새 토큰 저장
        }
        return new SignInResponse("로그인에 성공했습니다.",user.getUsername(),jwtTokenProvider.generateToken(signInRequest.username(),"access"),newRefreshToken);
    }

    // 휴대전화에 하이픈 추가 전처리
    private String formatCellPhoneNumber(String phoneNumber) {
        // 휴대전화가 숫자로만 이루어졌을 때, 하이픈을 자동으로 추가하는 로직
        return phoneNumber.replaceAll("^(\\+82|0)?(\\d{3})(\\d{4})(\\d{4})$", "$1$2-$3-$4");
    }
}
