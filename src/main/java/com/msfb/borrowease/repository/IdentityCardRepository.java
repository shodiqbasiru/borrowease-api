package com.msfb.borrowease.repository;

import com.msfb.borrowease.constant.EIdentityCard;
import com.msfb.borrowease.entity.IdentityCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentityCardRepository extends JpaRepository<IdentityCard, String> {
    Optional<IdentityCard> findByIdentityCard(EIdentityCard identityCard);
}
