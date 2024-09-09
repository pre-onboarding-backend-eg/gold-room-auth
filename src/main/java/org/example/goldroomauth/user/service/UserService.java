package org.example.goldroomauth.user.service;

import lombok.RequiredArgsConstructor;
import org.example.goldroomauth.user.domain.User;
import org.example.goldroomauth.user.dto.SignUpRequest;
import org.example.goldroomauth.user.dto.SignUpResponse;
import org.example.goldroomauth.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

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

    // 휴대전화에 하이픈 추가 전처리
    private String formatCellPhoneNumber(String phoneNumber) {
        // 휴대전화가 숫자로만 이루어졌을 때, 하이픈을 자동으로 추가하는 로직
        return phoneNumber.replaceAll("^(\\+82|0)?(\\d{3})(\\d{4})(\\d{4})$", "$1$2-$3-$4");
    }
}
