package com.dtb.cms.card.controller;

import com.dtb.cms.card.model.entity.Card;
import com.dtb.cms.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cms/api")
public class CardController {
    @Autowired
    private CardService service;

    @GetMapping("/cards")
    public List<Card> getCards(){
        return service.getCards();
    }
}
