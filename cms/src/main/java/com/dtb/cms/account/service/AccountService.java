package com.dtb.cms.account.service;

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

    public Page<Account> getAccounts(int page, int size, String iban, String bicSwift, String cardAlias){
        Pageable pageable = PageRequest.of(page, size);

        Specification<Account> ibanSpec = AccountSpecifications.ibanLike(iban);
        Specification<Account> bicSwiftSpec = AccountSpecifications.bicSwiftLike(bicSwift);
        Specification<Account> cardAliasSpec = AccountSpecifications.cardAliasLike(cardAlias);

        Specification<Account> fullSpec = Specification.where(ibanSpec).and(bicSwiftSpec).and(cardAliasSpec);

        return repo.findAll(fullSpec, pageable);
    }
}
