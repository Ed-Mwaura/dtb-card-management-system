package com.dtb.cms.account.model;

import com.dtb.cms.card.model.entity.Card;
import com.dtb.cms.customer.model.Customer;
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
    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private String iban;

    @Column(nullable = false)
    private String bicSwift;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Card> cards;

}
