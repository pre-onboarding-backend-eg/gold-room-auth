package org.example.goldroomauth.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id",nullable = false)
    private UUID userId;

    @Column(unique = true,nullable = false)
    @Size(min = 8, max = 50)
    private String username;

    @Column(nullable = false)
    @Size(min = 10, max = 200)
    private String password;

    @Column(nullable = false)
    @Size(max = 20)
    private String nickname;

    @Column(nullable = false)
    @Size(max = 20)
    private String phoneNumber;

    @Column(nullable = false)
    @Size(max = 20)
    private String zipCode;

    @Column(nullable = false)
    @Size(max = 200)
    private String address;

    @Column(nullable = false)
    @Size(max = 200)
    private String addressDetail;
}
