package org.example.goldroomauth.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.goldroomauth.user.dto.SignInRequest;
import org.example.goldroomauth.user.dto.SignInResponse;
import org.example.goldroomauth.user.dto.SignUpRequest;
import org.example.goldroomauth.user.dto.SignUpResponse;
import org.example.goldroomauth.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name="User",description = "사용자에 관련된 Api입니다.")
public class UserController {
    private final UserService userService;

    // 1. 회원가입
    @PostMapping("/sign-up")
    @Operation(
            summary = "회원가입",
            description = "회원가입을 진행합니다."
    )
    @ApiResponse(
            responseCode = "201",
            description = "회원가입에 성공하였습니다."
    )
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpResponse);
    }

    // 2. 로그인
    @PostMapping("/sign-in")
    @Operation(
            summary = "로그인",
            description = "로그인을 진행합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인에 성공하였습니다."
    )
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        SignInResponse signInResponse = userService.signIn(signInRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(signInResponse);
    }
}
