package com.dtb.cms.customer.controller;

import com.dtb.cms.customer.dto.CustomerDTO;
import com.dtb.cms.customer.dto.CustomerUpdateDTO;
import com.dtb.cms.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
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

    /**
     * endpoint to get customer by id
     * */
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long customerId){
        CustomerDTO result = service.getCustomerById(customerId);

        return ResponseEntity.ok(result);
    }

    /**
     * New customer endpoint
     * */
    @PostMapping("/add")
    public ResponseEntity<?> addCustomer(@RequestBody CustomerDTO reqBody){
        CustomerDTO newCustomer = service.addCustomer(reqBody);

        return ResponseEntity.ok(newCustomer);
    }

    /**
     * Customer update endpoint
     * */
    @PutMapping("/{customerId}")
    public  ResponseEntity<?> updateCustomer(@PathVariable Long customerId,
                                             @RequestBody CustomerUpdateDTO reqBody){

        CustomerDTO updatedCustomer = service.updateCustomer(customerId, reqBody);

        return ResponseEntity.ok(updatedCustomer);

    }

    /**
     * Delete customer endpoint
     * */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId){
        service.deleteCustomer(customerId);

        return ResponseEntity.noContent().build();
    }
}
