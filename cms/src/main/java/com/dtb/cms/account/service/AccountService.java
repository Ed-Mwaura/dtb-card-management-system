package com.dtb.cms.account.service;

import com.dtb.cms.account.dto.AccountDTO;
import com.dtb.cms.account.model.Account;
import com.dtb.cms.account.repository.AccountRepository;
import com.dtb.cms.account.specification.AccountSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repo;

    /**
     * Method to convert the account to DTO that defines what will be displayed.
     * */
    private AccountDTO toAccountDTO(Account account){
        return AccountDTO.builder()
                .accountId(account.getAccountId())
                .iban(account.getIban())
                .bicSwift(account.getBicSwift())
                .customerId(account.getCustomerId())
                .build();
    }

    public Page<AccountDTO> getAccounts(int page, int size, String iban, String bicSwift, String cardAlias){
        Pageable pageable = PageRequest.of(page, size);

        // call the filters
        Specification<Account> ibanSpec = AccountSpecifications.ibanLike(iban);
        Specification<Account> bicSwiftSpec = AccountSpecifications.bicSwiftLike(bicSwift);
        Specification<Account> cardAliasSpec = AccountSpecifications.cardAliasLike(cardAlias);

        // join the filters
        Specification<Account> fullSpec = Specification.where(ibanSpec).and(bicSwiftSpec).and(cardAliasSpec);

        Page<Account> rawData = repo.findAll(fullSpec, pageable);

        return rawData.map(this::toAccountDTO);
    }
}
