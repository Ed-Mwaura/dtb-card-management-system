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

@RestController
@RequestMapping("/cms/api")
public class AccountController {

    @Autowired
    private AccountService service;

    @GetMapping("/accounts")
    public ResponseEntity<?> getAccounts(@RequestParam Integer page, @RequestParam Integer size){
        Page<Account> results =  service.getAccounts(page, size);

        return ResponseEntity.ok(results);
    }
}
