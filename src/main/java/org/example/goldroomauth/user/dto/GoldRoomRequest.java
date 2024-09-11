package org.example.goldroomauth.user.dto;

public record GoldRoomRequest(String goldRoomName,
                              String description,
                              String goldRoomZipCode,
                              String goldRoomAddress,
                              String goldRoomAddressDetail) {
}
