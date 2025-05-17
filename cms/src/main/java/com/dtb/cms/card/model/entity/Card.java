package com.dtb.cms.card.model.entity;

import com.dtb.cms.account.model.Account;
import com.dtb.cms.card.model.key.CardId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Card {
    @EmbeddedId
    private CardId id;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false)
    private String pan;

    @Column(nullable = false)
    private String cvv;

    @Column(name = "account_id", insertable = false, updatable = false)
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;
}
