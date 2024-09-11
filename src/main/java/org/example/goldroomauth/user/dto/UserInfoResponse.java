package org.example.goldroomauth.user.dto;

import java.util.List;

public record UserInfoResponse(String message,
                               String username,
                               String nickname,
                               String phoneNumber,
                               String zipCode,
                               String address,
                               String addressDetail,
                               List<GoldRoomResponse> goldRoomResponses) {
}
