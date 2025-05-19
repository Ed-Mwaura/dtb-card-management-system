package com.dtb.cms.card.repository;

import com.dtb.cms.card.model.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {

    @Query("SELECT COUNT(c) FROM Card c WHERE c.accountId = :accountId")
    int countByAccountId(@Param("accountId") Long accountId);
}
