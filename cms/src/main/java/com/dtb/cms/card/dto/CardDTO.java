package com.dtb.cms.card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardDTO {
    private Long cardId;
    private String cardType;
    private String alias;
    private String pan;
    private String cvv;
    private Long accountId; // might remove later if not necessary. or modify to use account owner
}
