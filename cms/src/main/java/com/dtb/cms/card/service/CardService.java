package com.dtb.cms.card.service;

import com.dtb.cms.card.dto.CardDTO;
import com.dtb.cms.card.dto.CardUpdateDTO;
import com.dtb.cms.card.model.entity.Card;
import com.dtb.cms.card.model.enums.CardTypes;
import com.dtb.cms.card.model.key.CardId;
import com.dtb.cms.card.repository.CardRepository;
import com.dtb.cms.card.specification.CardSpecifications;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    @Autowired
    private CardRepository repo;

    /**
     * Method to mask the pan unless override param is passed
     * */
    private String maskPan(String pan, boolean override){
        if (override) {
            return pan;
        }
        else if(pan == null || pan.length() < 4) {
            return "****";
        } else{
            return "**** **** **** " + pan.substring(pan.length() - 4);
        }
    }

    /**
     * Method to mask the cvv unless override param is passed
     * */
    private String maskCvv(String cvv, boolean override){
        if(override) return cvv;
        return "***";
    }

    /**
     * Method to convert the card to DTO that defines what will be displayed.
     * */
    private CardDTO toCardDTO(Card card, boolean override){
        return CardDTO.builder()
                .cardId(card.getId().getCardId())
                .cardType(String.valueOf(card.getId().getCardType()))
                .alias(card.getAlias())
                .pan(maskPan(card.getPan(), override))
                .cvv(maskCvv(card.getCvv(), override))
                .accountId(card.getAccountId()) //TODO: REVIEW IF NEEDED
                .build();
    }

    /**
     * Method that retrieves a paginated list of cards that match the given filters
     * */
    public Page<CardDTO> getCards(int page, int size, String alias, String pan, String cardType, boolean override){
        Pageable pageable = PageRequest.of(page, size);

        // call the filters
        Specification<Card> aliasSpec = CardSpecifications.aliasLike(alias);
        Specification<Card> panSpec = CardSpecifications.panLike(pan);
        Specification<Card> cardTypeSpec = CardSpecifications.hasCardType(cardType);

        // join the filters
        Specification<Card> fullSpec = Specification.where(aliasSpec).and(panSpec).and(cardTypeSpec);

        Page<Card> rawData = repo.findAll(fullSpec, pageable);

        return rawData.map(card -> toCardDTO(card, override));
    }

    /**
     * Method that updates given card
     * */
    public CardDTO updateCard(Long cardId, CardTypes cardType, CardUpdateDTO reqBody){
        CardId id = new CardId(cardId, cardType);

        // throw error that will be handled by global exception handler once implemented
        //TODO: implement global exception handler
        Card card = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Card not found"));

        // update allowed fields
        if(reqBody.getAlias() != null) {
            card.setAlias(reqBody.getAlias());
        }
        if(reqBody.getPan() != null) {
            card.setPan(reqBody.getPan());
        }
        if(reqBody.getCvv() != null) {
            card.setCvv(reqBody.getCvv());
        }

        Card saved = repo.save(card);

        // return saved card with default value for override as false since it's not necessary for the update
        return toCardDTO(saved, false);
    }

    /**
     * Method to delete a given card
     * */
    public void deleteCard(Long cardId, CardTypes cardType){
        CardId id = new CardId(cardId, cardType);
        if(!repo.existsById(id)){
            throw new EntityNotFoundException("Card not found with ID: "+ cardId + ", type: " + cardType);
        }

        repo.deleteById(id);
    }
}
