package com.dtb.cms.card.service;

import com.dtb.cms.card.model.entity.Card;
import com.dtb.cms.card.repository.CardRepository;
import com.dtb.cms.card.specification.CardSpecifications;
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

    public Page<Card> getCards(int page, int size, String alias, String pan, String cardType){
        Pageable pageable = PageRequest.of(page, size);

        Specification<Card> aliasSpec = CardSpecifications.aliasLike(alias);
        Specification<Card> panSpec = CardSpecifications.panLike(pan);
        Specification<Card> cardTypeSpec = CardSpecifications.hasCardType(cardType);

        Specification<Card> fullSpec = Specification.where(aliasSpec).and(panSpec).and(cardTypeSpec);

        return repo.findAll(fullSpec, pageable);
    }
}
