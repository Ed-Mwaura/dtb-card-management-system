package com.dtb.cms.customer.service;

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
     * Method to set end date to actual end of day
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

    public Page<Customer> getCustomers(int page, int size, String name, Date start, Date end){
        // end date reads time of 00:00:00
        // entities created on that day will be filtered out - not intended
        // adjusting to include selected day
        end = adjustDate(end);

        Pageable pageable = PageRequest.of(page, size);

        Specification<Customer> fullNameSpec = CustomerSpecifications.fullNameContains(name);
        Specification<Customer> dateRangeSpec = CustomerSpecifications.createdBetween(start, end);

        Specification<Customer> finalSPec = Specification.where(fullNameSpec).and(dateRangeSpec);
        return repo.findAll(finalSPec, pageable);
    }
}
