package com.dtb.cms.account.controller;

import com.dtb.cms.account.model.Account;
import com.dtb.cms.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cms/api")
public class AccountController {

    @Autowired
    private AccountService service;

    @GetMapping("/accounts")
    public ResponseEntity<?> getAccounts(@RequestParam Integer page,
                                         @RequestParam Integer size,
                                         @RequestParam(required = false) String iban,
                                         @RequestParam(required = false) String bicSwift,
                                         @RequestParam(required = false) String cardAlias){
        Page<Account> pageResult =  service.getAccounts(page, size, iban, bicSwift, cardAlias);

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageResult.getContent());
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("pageSize", pageResult.getSize());

        return ResponseEntity.ok(response);
    }
}
