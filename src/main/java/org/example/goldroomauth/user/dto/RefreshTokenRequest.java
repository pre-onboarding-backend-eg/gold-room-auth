package org.example.goldroomauth.user.dto;

public record RefreshTokenRequest(String accessToken, String refreshToken) {
}
