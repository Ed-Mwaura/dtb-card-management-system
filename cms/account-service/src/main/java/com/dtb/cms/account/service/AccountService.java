package com.dtb.cms.account.service;

import com.dtb.cms.account.dto.AccountDTO;
import com.dtb.cms.account.exception.AccountOwnershipException;
import com.dtb.cms.account.model.Account;
import com.dtb.cms.account.repository.AccountRepository;
import com.dtb.cms.account.specification.AccountSpecifications;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repo;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Value("${card.service.url}")
    private String cardServiceUrl;

    /**
     * Method that calls the customers service to check if provided
     * customerId exists
     * */
    public void verifyCustomerExists(Long customerId){
        String url = customerServiceUrl + customerId;
        try{
            restTemplate.getForObject(url, Void.class);
        } catch (HttpClientErrorException.NotFound e){
            throw new EntityNotFoundException("Customer with id " + customerId + " not found!");
        }
    }

    /**
     * Method to call cards service to get list of accounts
     * whose cards match alias provided
     * */
    public List<Long> getAccountIdsByAlias(String alias){
        String url = cardServiceUrl + "by-alias?alias=" + alias;
        System.out.println(url);
        ResponseEntity<List<Long>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Long>>() {
                }
        );
        System.out.println(response.getBody());
        return response.getBody();
    }

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

        // join the filters
        Specification<Account> fullSpec = Specification.where(ibanSpec).and(bicSwiftSpec);

        // join alias filter if provided
        if(cardAlias != null && !cardAlias.isBlank()){
            List<Long> accountIds = getAccountIdsByAlias(cardAlias);
            Specification<Account> aliasSpec = AccountSpecifications.accountIdIn(accountIds);
            fullSpec = fullSpec.and(aliasSpec);
        }



        Page<Account> rawData = repo.findAll(fullSpec, pageable);

        return rawData.map(this::toAccountDTO);
    }

    /**
     * Method to find account by id
     * */
    public AccountDTO findAccountById(Long accountId){
        Account account = repo.findById(accountId).orElseThrow(()-> new EntityNotFoundException("Account not found"));

        return toAccountDTO(account);
    }

    /**
     * Method to check if account already exists either to the same
     * customer or a different customer
     * */
    public void validateUniqueness(Long accountId, Long customerId){
        Optional<Account> existingAccountOpt = repo.findById(accountId);
        if(existingAccountOpt.isPresent()){
            Account existingAccount = existingAccountOpt.get();

            if (!existingAccount.getCustomerId().equals(customerId)){
                throw new AccountOwnershipException("Account belongs to another customer!");
            } else {
                throw new AccountOwnershipException("Account already exists for this customer!");
            }
        }

    }
    /**
     * Method that creates a new account
     * */
    public AccountDTO addAccount(Long customerId, AccountDTO reqBody){

        verifyCustomerExists(customerId);
        validateUniqueness(reqBody.getAccountId(), customerId);

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
