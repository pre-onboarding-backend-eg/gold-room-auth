package org.example.goldroomauth.user.repository;

import org.example.goldroomauth.user.domain.GoldRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GoldRoomRepository extends JpaRepository<GoldRoom, UUID> {
    List<GoldRoom> findByUser_UserId(@Param("user_id") UUID userId);
}
