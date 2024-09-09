package org.example.goldroomauth.user.service;

import lombok.RequiredArgsConstructor;
import org.example.goldroomauth.exception.ErrorCode;
import org.example.goldroomauth.exception.NotFoundException;
import org.example.goldroomauth.user.domain.User;
import org.example.goldroomauth.user.domain.UserDetail;
import org.example.goldroomauth.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        return UserDetail.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.emptyList())  // 모든 사용자에게 동일한 권한 부여
                .build();
    }
}
