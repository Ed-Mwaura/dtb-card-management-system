package com.dtb.cms.card.service;

import com.dtb.cms.card.dto.CardDTO;
import com.dtb.cms.card.dto.CardUpdateDTO;
import com.dtb.cms.card.exception.CardLimitExceededException;
import com.dtb.cms.card.model.entity.Card;
import com.dtb.cms.card.model.enums.CardTypes;
import com.dtb.cms.card.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    private List<Card> allCards;
    private CardDTO validCardDTO;
    private CardUpdateDTO validCardUpdateDTO;

    @Mock
    private CardRepository repo;

    @Mock
    private RestTemplate restTemplate;

    @Spy
    @InjectMocks
    private CardService service;

    @BeforeEach
    void setup(){
        allCards = Arrays.asList(
                new Card(1L, CardTypes.valueOf("PHYSICAL"), "shopping card", "1234567887654321", "712", 10L),
                new Card(2L, CardTypes.valueOf("VIRTUAL"), "groceries card", "1230987887654321", "123", 10L),
                new Card(3L, CardTypes.valueOf("PHYSICAL"), "gaming card", "1234567887658767", "345", 11L)
        );

        validCardDTO = CardDTO.builder()
                .cardId(5L)
                .cardType(CardTypes.PHYSICAL)
                .alias("Test card")
                .pan("61726261727367272")
                .cvv("876")
                .accountId(15L)
                .build();

        validCardUpdateDTO = CardUpdateDTO.builder()
                .alias("card test")
                .pan("8727281726352612")
                .cvv("098")
                .build();
    }


    // ---------------------------------------------------------------------------------------------------------
    // getCards() Tests
    // ---------------------------------------------------------------------------------------------------------
    @Test
    void getCards_noFilters_overrideFalse_masksPanAndCvv() {
        // Arrange
        Page<Card> page = new PageImpl<>(allCards);
        when(repo.findAll(
                ArgumentMatchers.<Specification<Card>>any(),
                any(Pageable.class))
        ).thenReturn(page);

        // Act
        Page<CardDTO> result = service.getCards(0, 10, null, null, null, false);

        // Assert
        assertEquals(3, result.getTotalElements());
        CardDTO first = result.getContent().get(0);
        assertEquals("**** **** **** 4321", first.getPan());
        assertEquals("***", first.getCvv());

        CardDTO second = result.getContent().get(1);
        assertEquals("**** **** **** 4321", second.getPan());
        assertEquals("***", second.getCvv());
    }

    @Test
    void getCards_noFilters_overrideTrue_showsFullPanAndCvv() {
        // Arrange
        Page<Card> page = new PageImpl<>(allCards);
        when(repo.findAll(
                ArgumentMatchers.<Specification<Card>>any(),
                any(Pageable.class))
        ).thenReturn(page);

        // Act
        Page<CardDTO> result = service.getCards(0, 10, null, null, null, true);

        // Assert
        assertEquals(3, result.getTotalElements());
        CardDTO first = result.getContent().get(0);
        assertEquals("1234567887654321", first.getPan());
        assertEquals("712", first.getCvv());
    }

    @Test
    void getCards_emptyDatabase_returnsEmptyPage() {
        // Arrange
        when(repo.findAll(
                ArgumentMatchers.<Specification<Card>>any(),
                any(Pageable.class))
        ).thenReturn(new PageImpl<>(Collections.emptyList()));

        // Act
        Page<CardDTO> result = service.getCards(0, 10, null, null, null, false);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    // ---------------------------------------------------------------------------------------------------------
    // addCard() Tests
    // ---------------------------------------------------------------------------------------------------------

    @Test
    void addCard_nonExistentAccount_throwsEntityNotFound() {
        Long accountId = 999L;

        doThrow(new EntityNotFoundException("Account with id " + accountId + " not found!"))
                .when(service).verifyAccountExists(accountId);

        assertThrows(EntityNotFoundException.class, () -> service.addCard(accountId, validCardDTO));
    }

    @Test
    void addCard_duplicateCardType_throwsCardLimitExceeded() {
        Long accountId = 100L;

        doNothing().when(service).verifyAccountExists(accountId);
        when(repo.countByAccountId(accountId)).thenReturn(1); // Ok
        when(repo.countByAccountIdAndCardType(accountId, CardTypes.PHYSICAL)).thenReturn(1); // Already exists

        assertThrows(CardLimitExceededException.class, () -> service.addCard(accountId, validCardDTO));
    }

}
