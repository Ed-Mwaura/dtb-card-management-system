package com.dtb.cms.account.model;

import com.dtb.cms.card.model.entity.Card;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private String iban;

    @Column(nullable = false)
    private String bicSwift;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private List<Card> cards;

}
