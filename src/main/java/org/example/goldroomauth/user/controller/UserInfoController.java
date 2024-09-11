package org.example.goldroomauth.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.goldroomauth.config.JwtTokenProvider;
import org.example.goldroomauth.exception.ErrorCode;
import org.example.goldroomauth.exception.UnauthorizedException;
import org.example.goldroomauth.user.dto.GoldRoomRequest;
import org.example.goldroomauth.user.dto.GoldRoomResponse;
import org.example.goldroomauth.user.dto.UserInfoResponse;
import org.example.goldroomauth.user.service.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-info")
@RequiredArgsConstructor
@Tag(name="UserInformation",description = "사용자 개인정보와 관련된 Api입니다.")
public class UserInfoController {
    private final UserInfoService userInfoService;
    private final JwtTokenProvider jwtTokenProvider;

    // 1. 회원정보 불러오기
    @GetMapping("/mypage")
    public ResponseEntity<UserInfoResponse> getUserInfo(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }
        UserInfoResponse userInfoResponse = userInfoService.getUserInfoFromToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(userInfoResponse);
    }
    // 2. 금은방 정보 추가하기
    @PostMapping("/mypage/gold-room/add")
    public ResponseEntity<GoldRoomResponse> addGoldRoom(HttpServletRequest request,
                                                        @RequestBody GoldRoomRequest goldRoomRequest) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null){
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }
        GoldRoomResponse goldRoomResponse = userInfoService.addGoldRoomInfo(token,goldRoomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(goldRoomResponse);
    }
    // 3. 회원 탈퇴하기
}
