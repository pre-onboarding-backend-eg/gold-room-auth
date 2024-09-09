package org.example.goldroomauth.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(@NotBlank @Size(max = 50) String username,
                            @NotBlank @Size(min = 10, max = 200, message = "비밀번호는 최소 10자리 이상으로 설정해주세요.") String password,
                            @NotBlank @Size(max = 20) String nickname,
                            @NotBlank @Size(max = 20) String phoneNumber,
                            @NotBlank @Size(max = 20) String zipCode,
                            @NotBlank @Size(max = 200) String address,
                            @NotBlank @Size(max = 200) String addressDetail) {
}
