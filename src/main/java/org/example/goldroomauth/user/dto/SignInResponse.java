package org.example.goldroomauth.user.dto;

public record SignInResponse(String message, String username, String accessToken, String refreshToken) {
}
