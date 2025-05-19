package com.dtb.cms.card.repository;

import com.dtb.cms.card.model.entity.Card;
import com.dtb.cms.card.model.enums.CardTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {

    int countByAccountId(Long accountId);

    int countByAccountIdAndCardType(Long accountId, CardTypes cardType);

    List<Card> findByAliasContainingIgnoreCase(String alias);
}
