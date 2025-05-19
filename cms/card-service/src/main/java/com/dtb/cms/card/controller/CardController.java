package com.dtb.cms.card.controller;

import com.dtb.cms.card.dto.CardDTO;
import com.dtb.cms.card.dto.CardUpdateDTO;
import com.dtb.cms.card.model.enums.CardTypes;
import com.dtb.cms.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cms/api/cards")
public class CardController {
    @Autowired
    private CardService service;

    /**
     * Endpoint for paginated list of cards
     * */
    @GetMapping
    public ResponseEntity<?> getCards(@RequestParam Integer page,
                                      @RequestParam Integer size,
                                      @RequestParam(required = false) String alias,
                                      @RequestParam(required = false) String pan,
                                      @RequestParam(required = false) String cardType,
                                      @RequestParam(defaultValue = "false") boolean override){
        Page<CardDTO> pageResult =  service.getCards(page, size, alias, pan, cardType, override);

        // map to hold pagination metadata and returned content
        Map<String, Object> response = new HashMap<>();

        response.put("content", pageResult.getContent());
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("pageSize", pageResult.getSize());

        // return only ok response.
        // TODO: Create Global error handler for other response statuses & throw in service
        return ResponseEntity.ok(response);
    }

    /**
     * Add new card endpoint
     * */
    @PostMapping
    public ResponseEntity<?> addCard(@RequestParam Long accountId,
                                     @RequestBody CardDTO reqBody){

        CardDTO newCardObj = service.addCard(accountId, reqBody);
        return ResponseEntity.ok(newCardObj);
    }

    /**
     * Card update endpoint
     * */
    @PutMapping("/{cardId}/{cardType}")
    public ResponseEntity<?> updateCard(@PathVariable Long cardId,
                                        @PathVariable CardTypes cardType,
                                        @RequestBody CardUpdateDTO reqBody){

        CardDTO updated = service.updateCard(cardId, cardType, reqBody);

        return ResponseEntity.ok(updated);
    }

    /**
     * Delete card endpoint
     * */
    @DeleteMapping("/{cardId}/{cardType}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId,
                                           @PathVariable CardTypes cardType){

        //TODO: Because of using enum, card type must match case. Review whether to maintain this
        service.deleteCard(cardId, cardType);
        return ResponseEntity.noContent().build(); // 204
    }

    /**
     * Get account id list based on provided alias
     * */
    @GetMapping("/by-alias")
    public ResponseEntity<List<Long>> getAccountIdsByAlias(@RequestParam String alias){
        List<Long> results =  service.findAccountsByCardAlias(alias);

        return ResponseEntity.ok(results);
    }
}
