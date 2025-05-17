package com.dtb.cms.card.model.key;

import com.dtb.cms.card.model.enums.CardTypes;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardId implements Serializable {
    private Long cardId;

    @Enumerated(EnumType.STRING)
    private CardTypes cardType;
}
