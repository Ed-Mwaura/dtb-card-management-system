package com.dtb.cms.customer.controller;

import com.dtb.cms.customer.model.Customer;
import com.dtb.cms.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/api")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers(@RequestParam Integer page, @RequestParam Integer size){
        Page<Customer> results =  service.getCustomers(page, size);

        return ResponseEntity.ok(results);
    }
}
