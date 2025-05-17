package com.dtb.cms.account.specification;

import com.dtb.cms.account.model.Account;
import com.dtb.cms.card.model.entity.Card;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecifications {
    // iban, bicSwift and cardAlias
    public static Specification<Account> ibanLike (String iban){
        return (root, query, cb) -> {
            if(iban == null){
                return cb.conjunction(); // no filter if not provided
            }

            String pattern = "%" +iban.toLowerCase()+ "%";

            return cb.like(cb.lower(root.get("iban")), pattern);
        };
    }

    public static Specification<Account> bicSwiftLike(String bicSwift){
        return (root, query, cb) -> {
            if(bicSwift == null){
                return cb.conjunction();
            }

            String pattern = "%" + bicSwift.toLowerCase() + "%";

            return cb.like(cb.lower(root.get("bicSwift")), pattern);
        };
    }

    public static Specification<Account> cardAliasLike(String cardAlias){
        return (root, query, criteriaBuilder) -> {
            if(cardAlias == null){
                return criteriaBuilder.conjunction();
            }

            // join with card table to get card alias
            Join<Account, Card> cardJoin = root.join("cards", JoinType.INNER);

            String pattern = "%" + cardAlias.toLowerCase() + "%";

            return criteriaBuilder.like(criteriaBuilder.lower(cardJoin.get("alias")), pattern);
        };
    }
}
