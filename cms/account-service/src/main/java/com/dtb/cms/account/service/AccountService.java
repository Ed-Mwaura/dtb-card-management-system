package com.dtb.cms.account.service;

import com.dtb.cms.account.dto.AccountDTO;
import com.dtb.cms.account.model.Account;
import com.dtb.cms.account.repository.AccountRepository;
import com.dtb.cms.account.specification.AccountSpecifications;
import jakarta.persistence.EntityNotFoundException;
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

    /**
     * Method that retrieves a paginated list of accounts that match the given filters
     * */
    public Page<AccountDTO> getAccounts(int page, int size, String iban, String bicSwift, String cardAlias){
        Pageable pageable = PageRequest.of(page, size);

        // call the filters
        Specification<Account> ibanSpec = AccountSpecifications.ibanLike(iban);
        Specification<Account> bicSwiftSpec = AccountSpecifications.bicSwiftLike(bicSwift);
//        Specification<Account> cardAliasSpec = AccountSpecifications.cardAliasLike(cardAlias);

        // join the filters
        Specification<Account> fullSpec = Specification.where(ibanSpec).and(bicSwiftSpec); //.and(cardAliasSpec);

        Page<Account> rawData = repo.findAll(fullSpec, pageable);

        return rawData.map(this::toAccountDTO);
    }

    /**
     * Method that creates a new account
     * */
    public AccountDTO addAccount(Long customerId, AccountDTO reqBody){
//        Customer customer = customerRepo.findById(customerId).orElseThrow(()->new EntityNotFoundException("Customer not found"));

        Account newAccount = new Account();

        newAccount.setAccountId(reqBody.getAccountId());
        newAccount.setIban(reqBody.getIban());
        newAccount.setBicSwift(reqBody.getBicSwift());
        newAccount.setCustomerId(customerId);

        return toAccountDTO(repo.save(newAccount));
    }

    //TODO: None of the attributes of account seem editable. Confirm.

    /**
     * Method to delete a given account
     * */
    public void deleteAccount(Long accountId){
        if(!repo.existsById(accountId)){
            throw new EntityNotFoundException("Account not found with ID: " + accountId);
        }

        repo.deleteById(accountId);
    }

}
