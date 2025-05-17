package com.dtb.cms.card.model.entity;

import com.dtb.cms.account.model.Account;
import com.dtb.cms.card.model.key.CardId;
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
    @Column(nullable = false)
    private CardId id;

    @Column(nullable = false)
    private String alias;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private String pan;

    @Column(nullable = false)
    private String cvv;
}
