package com.dtb.cms.card.repository;

import com.dtb.cms.card.model.entity.Card;
import com.dtb.cms.card.model.key.CardId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, CardId> {
}
