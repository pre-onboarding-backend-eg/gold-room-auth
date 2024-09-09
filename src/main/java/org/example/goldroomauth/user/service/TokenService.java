package org.example.goldroomauth.user.service;

import lombok.RequiredArgsConstructor;
import org.example.goldroomauth.exception.BadRequestException;
import org.example.goldroomauth.exception.UnauthorizedException;
import org.example.goldroomauth.user.dto.AccessTokenResponse;
import org.springframework.security.core.Authentication;
import org.example.goldroomauth.config.JwtTokenProvider;
import org.example.goldroomauth.exception.ErrorCode;
import org.example.goldroomauth.exception.NotFoundException;
import org.example.goldroomauth.user.domain.Token;
import org.example.goldroomauth.user.domain.User;
import org.example.goldroomauth.user.domain.UserDetail;
import org.example.goldroomauth.user.dto.RefreshTokenRequest;
import org.example.goldroomauth.user.dto.RefreshTokenResponse;
import org.example.goldroomauth.user.repository.TokenRepository;
import org.example.goldroomauth.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    // refreshToken이 만료된 경우 재발급
    @Transactional
    public RefreshTokenResponse reissueRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        // 1. refreshToken 만료 체크
        if (!tokenProvider.validToken(refreshTokenRequest.refreshToken())){
            throw new UnauthorizedException(ErrorCode.REFRESH_TOKEN_EXPIRATION);
        }
        // 2. 사용자 권한 확인
        User user = findUserByToken(refreshTokenRequest);
        // 3. 토큰을 활용해 사용자 조회
        Token storedToken = tokenRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_TOKEN));
        // 4. 토큰 재발급
        String accessToken = tokenProvider.generateToken(user.getUsername(),"access");
        String refreshToken = tokenProvider.generateToken(user.getUsername(),"refresh");
       // 5. refreshToken 새로 db 업데이트
        storedToken.updateToken(refreshToken);
        tokenRepository.save(storedToken);
         // 6. RefreshtokenResponse 생성 및 반환
        return new RefreshTokenResponse(accessToken, refreshToken);
    }

    // accessToken 재발급
    @Transactional
    public AccessTokenResponse reissueAccessToken(String refreshToken) {
        // 1. refreshToken 만료 체크
        if (!tokenProvider.validToken(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.REFRESH_TOKEN_EXPIRATION);
        }
        // 2. refreshToken을 활용해 사용자 username 가져오기
        String username = tokenProvider.getUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        // 3. 데이터베이스에 저장된 refreshToken과 일치하는지 확인
        Token storedToken = tokenRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_TOKEN));
        if (!storedToken.getRefreshToken().equals(refreshToken)) {
            throw new BadRequestException(ErrorCode.INVALID_TOKEN);
        }
        // 4. 새로운 액세스 토큰만 생성
        String newAccessToken = tokenProvider.generateToken(username, "access");
        // 5. AccessTokenResponse 생성 및 반환
        return new AccessTokenResponse("accessToken 성공적으로 발급됐습니다.",username,newAccessToken);
    }

    // 토큰으로 사용자 찾기
    @Transactional
    public User findUserByToken(RefreshTokenRequest refreshTokenRequest) {
        // 1. 권한이 있는 사용자인지 확인
        Authentication authentication = tokenProvider.getAuthentication(refreshTokenRequest.accessToken());
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        String username = userDetail.getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }


}
