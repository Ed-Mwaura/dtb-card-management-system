package com.dtb.cms.customer.service;

import com.dtb.cms.customer.dto.CustomerDTO;
import com.dtb.cms.customer.dto.CustomerUpdateDTO;
import com.dtb.cms.customer.model.Customer;
import com.dtb.cms.customer.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private List<Customer> allCustomers;
    private CustomerDTO validCustomerDTO;

    @Mock
    private CustomerRepository repo;

    @InjectMocks
    private CustomerService service;

    @BeforeEach
    void setUp(){
        allCustomers = Arrays.asList(
                new Customer(1L, "Edwin", "Ngugi", "Mwaura", new Date()),
                new Customer(2L, "Sheila", "Wairimu", "Wambui", new Date()),
                new Customer(3L, "Frank", "Opiyo", "Apiyo", new Date()),
                new Customer(4L, "Alex", "Nduati", "Mukeni", new Date()),
                new Customer(5L, "Charles", "Kip", "Koech", new Date())
        );

        validCustomerDTO = CustomerDTO.builder()
                .customerId(10L)
                .firstName("John")
                .lastName("Mutembei")
                .otherName("Sang")
                .build();
    }


    // ---------------------------------------------------------------------------------------------------------
    // getCustomers() Tests
    // ---------------------------------------------------------------------------------------------------------
    @Test
    void getCustomersNoFilters_returnsAllCustomersPaginated(){

        Page<Customer> mockPage = new PageImpl<>(allCustomers);
        when(repo.findAll((Specification<Customer>) any(), any(Pageable.class))).thenReturn(mockPage);

        Page<CustomerDTO> result = service.getCustomers(0, 10, null, null, null);

        assertEquals(5, result.getTotalElements());


    }

    @Test
    void getCustomers_returnsEmptyPageWhenNoCustomers(){
        Page<Customer> emptyPage = new PageImpl<>(List.of());
        when(repo.findAll((Specification<Customer>) any(), any(Pageable.class))).thenReturn(emptyPage);

        // act
        Page<CustomerDTO> result = service.getCustomers(0, 10, null, null, null);

        //assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getCustomers_startGreaterThanEnd_throwsIllegalArgumentException() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse("2025-05-15");
        Date end = sdf.parse("2025-05-14");

        assertThrows(IllegalArgumentException.class,
                () -> {
            service.getCustomers(0, 10, null, start, end);
                });
    }


    // ---------------------------------------------------------------------------------------------------------
    // getCustomersById tests
    // ---------------------------------------------------------------------------------------------------------

    @Test
    void getCustomersById_idNotExists_throwsEntityNotFoundException(){
        assertThrows(EntityNotFoundException.class,
                () -> {
                    service.getCustomerById(99L);
                });
    }

    @Test
    void deleteCustomersById_idNotExists_throwsEntityNotFoundException(){
        assertThrows(EntityNotFoundException.class,
                () -> {
                    service.deleteCustomer(99L);
                });
    }


    // ---------------------------------------------------------------------------------------------------------
    // addCustomer() tests
    // ---------------------------------------------------------------------------------------------------------

    @Test
    void addCustomer_withValidData_returnsSavedCustomer() {
        Customer saved = new Customer(100L, "John", "Doe", "Smith", new Date());

        when(repo.save(any(Customer.class))).thenReturn(saved);

        CustomerDTO result = service.addCustomer(validCustomerDTO);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void addCustomer_withNullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.addCustomer(null));
    }

    @Test
    void addCustomer_withMissingFirstName_throwsException() {
        validCustomerDTO.setFirstName(null);
        assertThrows(Exception.class, () -> service.addCustomer(validCustomerDTO));
    }

    @Test
    void addCustomer_withMissingLastName_throwsException() {
        validCustomerDTO.setLastName(null);
        assertThrows(Exception.class, () -> service.addCustomer(validCustomerDTO));
    }

    @Test
    void addCustomer_withNullOtherName_savesSuccessfully() {
        validCustomerDTO.setOtherName(null);
        when(repo.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerDTO result = service.addCustomer(validCustomerDTO);
        assertNull(result.getOtherName());
    }


    // ---------------------------------------------------------------------------------------------------------
    // updateCustomer() tests
    // ---------------------------------------------------------------------------------------------------------
    @Test
    void updateCustomer_withAllFieldsUpdated_savesAndReturnsUpdatedCustomer() {
        Customer existing = new Customer(1L, "Old", "Name", "Oldie", new Date());
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO("New", "Name", "Newie");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerDTO result = service.updateCustomer(1L, updateDTO);

        assertEquals("New", result.getFirstName());
        assertEquals("Name", result.getLastName());
        assertEquals("Newie", result.getOtherName());
    }

    @Test
    void updateCustomer_withSomeNullFields_onlyNonNullFieldsAreUpdated() {
        Customer existing = new Customer(1L, "Mercy", "Muringe", "Mwega", new Date());
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO("Janet", null, null); // only first name changes

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerDTO result = service.updateCustomer(1L, updateDTO);

        assertEquals("Janet", result.getFirstName());
        assertEquals("Muringe", result.getLastName());          // unchanged
        assertEquals("Mwega", result.getOtherName());       // unchanged
    }

    @Test
    void updateCustomer_withNonexistentId_throwsEntityNotFoundException() {
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO("Edwin", "Ngugi", null);

        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateCustomer(999L, updateDTO));
    }


    // ---------------------------------------------------------------------------------------------------------
    // deleteCustomer() tests
    // ---------------------------------------------------------------------------------------------------------


    @Test
    void deleteCustomer_withExistingId_deletesSuccessfully() {
        Long customerId = 1L;

        when(repo.existsById(customerId)).thenReturn(true);
        doNothing().when(repo).deleteById(customerId);

        assertDoesNotThrow(() -> service.deleteCustomer(customerId));
        verify(repo).deleteById(customerId);
    }

    @Test
    void deleteCustomer_withNonexistentId_throwsEntityNotFoundException() {
        Long customerId = 999L;

        when(repo.existsById(customerId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.deleteCustomer(customerId));

        assertEquals("Card not found with ID: " + customerId, exception.getMessage());
        verify(repo, never()).deleteById(any());
    }

    @Test
    void deleteCustomer_withNullId_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> service.deleteCustomer(null));
    }

}