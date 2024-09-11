package com.msfb.borrowease.repository;

import com.msfb.borrowease.entity.IdentityCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityCardRepository extends JpaRepository<IdentityCard, String> {
}
