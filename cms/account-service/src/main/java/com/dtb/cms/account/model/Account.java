package com.dtb.cms.account.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private Long customerId;

}
