package com.example.userauthenticationservice.repositories;

import com.example.userauthenticationservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Session save(Session session);

    Optional<Session> findSessionByTokenAndUser_Id(String token, Long userId);
}
