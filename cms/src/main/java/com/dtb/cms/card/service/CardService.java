package com.dtb.cms.card.service;

import com.dtb.cms.card.model.entity.Card;
import com.dtb.cms.card.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    @Autowired
    private CardRepository repo;

    public List<Card> getCards(){
        return repo.findAll();
    }
}
