package org.example.goldroomauth.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.goldroomauth.user.dto.AccessTokenResponse;
import org.example.goldroomauth.user.dto.RefreshTokenRequest;
import org.example.goldroomauth.user.dto.RefreshTokenResponse;
import org.example.goldroomauth.user.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
@Tag(name="Token",description = "토큰발급 관련 Api입니다.")
public class TokenController {

    private final TokenService tokenService;

    // 리프레시토큰 재발급
    @PostMapping("/reissue-refresh")
    @Operation(
            summary = "리프레시토큰 재발급",
            description = "리프레시토큰 재발급을 진행합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "리프레시토큰 재발급에 성공하였습니다."
    )
    public ResponseEntity<RefreshTokenResponse> reissueRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenResponse refreshTokenResponse = tokenService.reissueRefreshToken(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(refreshTokenResponse);
    }

    // 액세스토큰 재발급
    @PostMapping("/reissue-access")
    @Operation(
            summary = "액세스토큰 재발급",
            description = "액세스토큰 재발급을 진행합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "액세토큰 재발급에 성공하였습니다."
    )
    public ResponseEntity<AccessTokenResponse> reissueAccessToken(@RequestParam String refreshToken) {
        AccessTokenResponse accessTokenResponse = tokenService.reissueAccessToken(refreshToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(accessTokenResponse);
    }
}
