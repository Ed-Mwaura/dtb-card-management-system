package com.dtb.cms.customer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String otherName;

    // added since we need date created for the filter
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date dateCreated;

}
