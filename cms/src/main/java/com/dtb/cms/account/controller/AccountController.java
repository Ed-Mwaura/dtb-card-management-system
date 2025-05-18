package com.dtb.cms.account.controller;

import com.dtb.cms.account.dto.AccountDTO;
import com.dtb.cms.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cms/api/accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    /**
     * Endpoint for paginated list of accounts
     * */
    @GetMapping
    public ResponseEntity<?> getAccounts(@RequestParam Integer page,
                                         @RequestParam Integer size,
                                         @RequestParam(required = false) String iban,
                                         @RequestParam(required = false) String bicSwift,
                                         @RequestParam(required = false) String cardAlias){
        Page<AccountDTO> pageResult =  service.getAccounts(page, size, iban, bicSwift, cardAlias);

        // map to hold pagination metadata and account content
        Map<String, Object> response = new HashMap<>();

        response.put("content", pageResult.getContent());
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("pageSize", pageResult.getSize());

        return ResponseEntity.ok(response);
    }

    /**
     * Delete card endpoint
     * */
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId){
        service.deleteAccount(accountId);
        return ResponseEntity.noContent().build(); // 204
    }
}
