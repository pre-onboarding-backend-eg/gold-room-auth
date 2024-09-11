package org.example.goldroomauth.user.dto;

public record GoldRoomResponse(String message,
                               String goldRoomName,
                               String description,
                               String goldRoomZipCode,
                               String goldRoomAddress,
                               String goldRoomAddressDetail) {
}
