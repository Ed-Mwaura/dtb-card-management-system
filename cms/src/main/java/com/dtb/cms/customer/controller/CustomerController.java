package com.dtb.cms.customer.controller;

import com.dtb.cms.customer.dto.CustomerDTO;
import com.dtb.cms.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cms/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping
    public ResponseEntity<?> getCustomers(@RequestParam Integer page,
                                          @RequestParam Integer size,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end){
        Page<CustomerDTO> pageResult =  service.getCustomers(page, size, name, start, end);

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageResult.getContent());
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("pageSize", pageResult.getSize());

        return ResponseEntity.ok(response);
    }
}
