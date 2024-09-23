package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.IdentityCard;

import java.util.List;

public interface IdentityCardService {
    IdentityCard createIdentityCard(IdentityCard identityCard);
    IdentityCard getById(String id);
    List<IdentityCard> getAllIdentityCards();
    IdentityCard updateIdentityCard(IdentityCard identityCard);
    void deleteIdentityCard(String id);
}
