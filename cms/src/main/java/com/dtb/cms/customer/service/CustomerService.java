package com.dtb.cms.customer.service;

import com.dtb.cms.customer.dto.CustomerDTO;
import com.dtb.cms.customer.model.Customer;
import com.dtb.cms.customer.repository.CustomerRepository;
import com.dtb.cms.customer.specification.CustomerSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repo;

    /**
     * Method to set end date to actual end of day. (23:59:59:999)
     * By default, Date comes with time of 00:00:00, which will affect
     * fetched results if the specified end day contains data
     * */
    private Date adjustDate(Date dateToAdjust){
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToAdjust);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND,999);

        return cal.getTime();
    }

    /**
     * Method to convert the customer to DTO that defines what will be displayed.
     * */
    private CustomerDTO toCustomerDTO(Customer customer){
        return CustomerDTO.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .otherName(customer.getOtherName())
                .dateCreated(customer.getDateCreated())
                .build();
    }

    public Page<CustomerDTO> getCustomers(int page, int size, String name, Date start, Date end){

        // adjusting end day to be inclusive of end day by setting to actual end of day
        if (end != null) {
            end = adjustDate(end);
        }
        Pageable pageable = PageRequest.of(page, size);

        // call the filters
        Specification<Customer> fullNameSpec = CustomerSpecifications.fullNameContains(name);
        Specification<Customer> dateRangeSpec = CustomerSpecifications.createdBetween(start, end);

        // join the filters
        Specification<Customer> finalSPec = Specification.where(fullNameSpec).and(dateRangeSpec);

        Page<Customer> rawResults = repo.findAll(finalSPec, pageable);

        return rawResults.map(this::toCustomerDTO);
    }
}
