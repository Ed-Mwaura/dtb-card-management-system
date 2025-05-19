package com.dtb.cms.account.service;

import com.dtb.cms.account.dto.AccountDTO;
import com.dtb.cms.account.exception.AccountOwnershipException;
import com.dtb.cms.account.model.Account;
import com.dtb.cms.account.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private List<Account> allAccounts;
    private AccountDTO validAccountDTO;

    @Mock
    private AccountRepository repo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountService service;

    @BeforeEach
    void setup(){
        allAccounts = Arrays.asList(
                new Account(1L, "IBAN1", "BIC1", 101L),
                new Account(2L, "IBAN2", "BIC2", 102L),
                new Account(3L, "IBAN3", "BIC3", 103L)
        );

        validAccountDTO = AccountDTO.builder()
                .accountId(10L)
                .iban("FEABAVFA")
                .bicSwift("AFAFAFA")
                .customerId(100L)
                .build();
    }

    // ---------------------------------------------------------------------------------------------------------
    // getAccounts() Tests
    // ---------------------------------------------------------------------------------------------------------

    @Test
    void getAccounts_withNoFilters_returnsAllAccounts() {
        Page<Account> page = new PageImpl<>(allAccounts);

        when(repo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<AccountDTO> result = service.getAccounts(0, 10, null, null, null);

        assertEquals(3, result.getTotalElements());
        assertEquals("IBAN1", result.getContent().get(0).getIban());
        verify(repo).findAll(any(Specification.class), any(Pageable.class));
    }

    // ---------------------------------------------------------------------------------------------------------
    // findAccountById() Tests
    // ---------------------------------------------------------------------------------------------------------

    @Test
    void findAccountById_withExistingId_returnsAccountDTO() {
        Account account = new Account(10L, "IBAN-10", "BIC-10", 100L); //TODO: REVIEW
        when(repo.findById(10L)).thenReturn(Optional.of(account));

        AccountDTO result = service.findAccountById(10L);

        assertEquals(10L, result.getAccountId());
        assertEquals("IBAN-10", result.getIban());
        verify(repo).findById(10L);
    }


    // ---------------------------------------------------------------------------------------------------------
    // deleteAccount() Tests
    // ---------------------------------------------------------------------------------------------------------
    @Test
    void deleteCustomersById_idNotExists_throwsEntityNotFoundException(){
        assertThrows(EntityNotFoundException.class,
                () -> {
                    service.deleteAccount(99L);
                });
    }


}