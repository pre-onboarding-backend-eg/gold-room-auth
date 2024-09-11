package org.example.goldroomauth.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Getter
@Table(name = "gold_rooms")
public class GoldRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "gold_room_id", nullable = false)
    private String goldRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String goldRoomName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Size(max = 20)
    private String goldRoomZipCode;

    @Column(nullable = false)
    @Size(max = 200)
    private String goldRoomAddress;

    @Column(nullable = false)
    @Size(max = 200)
    private String goldRoomAddressDetail;
}
