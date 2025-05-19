package com.dtb.cms.card.dto;

import com.dtb.cms.card.model.enums.CardTypes;
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
    private CardTypes cardType;
    private String alias;
    private String pan;
    private String cvv;
    private Long accountId;
}
