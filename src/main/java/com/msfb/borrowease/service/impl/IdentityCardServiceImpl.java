package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.entity.IdentityCard;
import com.msfb.borrowease.repository.IdentityCardRepository;
import com.msfb.borrowease.service.IdentityCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IdentityCardServiceImpl implements IdentityCardService {

    private final IdentityCardRepository repository;

    @Autowired
    public IdentityCardServiceImpl(IdentityCardRepository repository) {
        this.repository = repository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IdentityCard createIdentityCard(IdentityCard identityCard) {
        return repository.saveAndFlush(identityCard);
    }

    @Transactional(readOnly = true)
    @Override
    public IdentityCard getById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identity Card not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<IdentityCard> getAllIdentityCards() {
        return repository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IdentityCard updateIdentityCard(IdentityCard identityCard) {
        IdentityCard currentCard = getById(identityCard.getId());
        currentCard.setIdentityNumber(identityCard.getIdentityNumber());
        currentCard.setIdentityCard(identityCard.getIdentityCard());
        return currentCard;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteIdentityCard(String id) {
        repository.deleteById(id);
    }
}
