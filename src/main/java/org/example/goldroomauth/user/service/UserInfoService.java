package org.example.goldroomauth.user.service;

import lombok.RequiredArgsConstructor;
import org.example.goldroomauth.config.JwtTokenProvider;
import org.example.goldroomauth.exception.BadRequestException;
import org.example.goldroomauth.exception.ErrorCode;
import org.example.goldroomauth.user.domain.GoldRoom;
import org.example.goldroomauth.user.domain.User;
import org.example.goldroomauth.user.dto.GoldRoomRequest;
import org.example.goldroomauth.user.dto.GoldRoomResponse;
import org.example.goldroomauth.user.dto.UserInfoResponse;
import org.example.goldroomauth.user.repository.GoldRoomRepository;
import org.example.goldroomauth.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserRepository userRepository;
    private final GoldRoomRepository goldRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //  회원 정보 가져오기
    @Transactional
    public UserInfoResponse getUserInfoFromToken(String token) {
        // 1. 토큰 유효성 검사
        if (!jwtTokenProvider.validToken(token)) {
            throw new BadRequestException(ErrorCode.INVALID_TOKEN);
        }
        // 2. 토큰에서 username 가져오기
        String username = jwtTokenProvider.getUsername(token);
        // 3. username으로 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));
        // 4. user 정보를 기반으로 금은방 정보 조회
        List<GoldRoom> goldRoomList = goldRoomRepository.findByUser_UserId(user.getUserId());
        // 5. 금은방 정보 파싱
        List<GoldRoomResponse> goldRoomResponses;
        if (goldRoomList.isEmpty()){
            // 정보가 없는 경우
            goldRoomResponses = List.of(new GoldRoomResponse("조회된 금은방이 없습니다.", null, null, null, null, null));
        }
        else{
            // 정보가 있는 경우
            goldRoomResponses = goldRoomList.stream()
                    .map(gr -> new GoldRoomResponse(
                            "등록하신 금은방 정보입니다.",
                            gr.getGoldRoomName(),
                            gr.getDescription(),
                            gr.getGoldRoomZipCode(),
                            gr.getGoldRoomAddress(),
                            gr.getGoldRoomAddressDetail()
                    ))
                    .collect(Collectors.toList());
        }
        // 6. UserInfoResponse 생성 및 반환
            return new UserInfoResponse(
                    "회원정보가 성공적으로 조회됐습니다.",
                    user.getUsername(),
                    user.getNickname(),
                    user.getPhoneNumber(),
                    user.getZipCode(),
                    user.getAddress(),
                    user.getAddressDetail(),
                    goldRoomResponses
            );
        }
    // 금은방 정보 입력
    public GoldRoomResponse addGoldRoomInfo(String token, GoldRoomRequest goldRoomRequest) {
        // 1. 토큰 유효성 검사
        if (!jwtTokenProvider.validToken(token)) {
            throw new BadRequestException(ErrorCode.INVALID_TOKEN);
        }
        // 2. 토큰에서 username 가져오기
        String username = jwtTokenProvider.getUsername(token);
        // 3. username으로 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));
        // 4. 금은방 정보 입력
        if (goldRoomRequest == null) {
            throw new BadRequestException(ErrorCode.INVALID_GOLD_ROOM_REQUEST_BODY);
        }
        GoldRoom goldRoom = GoldRoom.builder()
                .goldRoomName(goldRoomRequest.goldRoomName())
                .description(goldRoomRequest.description())
                .goldRoomZipCode(goldRoomRequest.goldRoomZipCode())
                .goldRoomAddress(goldRoomRequest.goldRoomAddress())
                .goldRoomAddressDetail(goldRoomRequest.goldRoomAddressDetail())
                .user(user)
                .build();
        // 5. 금은방 정보 저장
        goldRoomRepository.save(goldRoom);
        // 6. GoldRoomResponse 생성 및 반환
        return new GoldRoomResponse("금은방 정보가 성공적으로 등록됐습니다.",
                goldRoom.getGoldRoomName(),
                goldRoom.getDescription(),
                goldRoom.getGoldRoomZipCode(),
                goldRoom.getGoldRoomAddress(),
                goldRoom.getGoldRoomAddressDetail());
    }
    //  회원 탈퇴

    /**
     * 토큰으로 사용자 권한을 검사하고 해당 사용자를 반환합니다.
     * @param token JWT 토큰
     * @return 인증된 사용자
     * @throws BadRequestException 토큰이 유효하지 않거나 사용자를 찾을 수 없는 경우
     */
    public User validateTokenAndGetUser(String token) {
        // 1. 토큰 유효성 검사
        if (!jwtTokenProvider.validToken(token)) {
            throw new BadRequestException(ErrorCode.INVALID_TOKEN);
        }
        // 2. 토큰에서 username 가져오기
        String username = jwtTokenProvider.getUsername(token);
        // 3. username으로 사용자 조회
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));
    }
}
