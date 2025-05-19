package com.dtb.cms.customer.specification;

import com.dtb.cms.customer.model.Customer;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Specifications class for customers.
 * Defines the filters to be applied on customers api
 * */
public class CustomerSpecifications {
    public static Specification<Customer> fullNameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.trim().isEmpty()) return cb.conjunction(); // no filtering if name is empty

            String[] tokens = name.toLowerCase().trim().split("\\s+");
            List<Predicate> tokenPredicates = new ArrayList<>();

            for (String token : tokens) {
                String pattern = "%" + token + "%";
                Predicate match = cb.or(
                        cb.like(cb.lower(root.get("firstName")), pattern),
                        cb.like(cb.lower(root.get("lastName")), pattern),
                        cb.like(cb.lower(root.get("otherName")), pattern)
                );
                tokenPredicates.add(match);
            }

            return cb.and(tokenPredicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Customer> createdBetween(Date start, Date end){
        return (root, query, cb) -> {
            if(start == null && end == null){
                return cb.conjunction(); // no filter if no range provided
            }
            Path<Date> path = root.get("dateCreated");

            if(start != null && end != null){
                return cb.between(path, start, end);
            } else if(start != null){
                return cb.greaterThanOrEqualTo(path, start);
            } else{
                return cb.lessThanOrEqualTo(path, end);
            }
        };
    }
}
