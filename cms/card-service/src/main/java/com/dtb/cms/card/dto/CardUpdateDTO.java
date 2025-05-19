package com.dtb.cms.card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardUpdateDTO {
    private String alias;
    private String pan; //TODO: Confirm if editable
    private String cvv; //TODO: Confirm if editable
}
