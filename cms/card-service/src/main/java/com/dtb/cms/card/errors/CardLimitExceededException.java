package com.dtb.cms.card.errors;

public class CardLimitExceededException extends RuntimeException{
    public CardLimitExceededException(String message){
        super(message);
    }
}
