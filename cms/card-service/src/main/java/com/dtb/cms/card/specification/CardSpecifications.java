package com.dtb.cms.card.specification;

import com.dtb.cms.card.model.entity.Card;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications class for cards.
 * Defines the filters to be applied on cards api
 * */
public class CardSpecifications {
    public static Specification<Card> aliasLike(String alias){
        return (root, query, cb) -> {
            if(alias == null){
                return cb.conjunction(); // no filter if alias not provided
            }

            String pattern = "%" + alias.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("alias")), pattern);
        };
    }

    public static Specification<Card> hasCardType(String cardType){
        return (root, query, cb) ->{
           if(cardType == null){
               return cb.conjunction();
           }
           return cb.equal(root.get("id").get("cardType"), cardType);
        } ;
    }

    public static Specification<Card> panLike(String pan){
        return (root, query, cb) ->{
            if(pan == null){
                return cb.conjunction();
            }
            String pattern = "%" + pan.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("pan")), pattern);
        };
    }
}
