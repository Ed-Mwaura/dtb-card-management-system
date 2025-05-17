package com.dtb.cms.card.controller;

import com.dtb.cms.card.model.entity.Card;
import com.dtb.cms.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/api")
public class CardController {
    @Autowired
    private CardService service;

    @GetMapping("/cards")
    public ResponseEntity<?> getCards(@RequestParam Integer page,
                                      @RequestParam Integer size,
                                      @RequestParam(required = false) String alias,
                                      @RequestParam(required = false) String pan,
                                      @RequestParam(required = false) String cardType){
        Page<Card> results =  service.getCards(page, size, alias, pan, cardType);

        return ResponseEntity.ok(results);
    }
}
