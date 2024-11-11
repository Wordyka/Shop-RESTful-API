package com.wide.simple_spring_restful_api.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.wide.simple_spring_restful_api.model.AccountResponse;
import com.wide.simple_spring_restful_api.model.CustomerResponse;
import com.wide.simple_spring_restful_api.model.dto.PagingCustomerRequest;
import com.wide.simple_spring_restful_api.model.dto.UpdateCustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.wide.simple_spring_restful_api.entity.Customer;
import com.wide.simple_spring_restful_api.model.dto.CreateCustomerRequest;
import com.wide.simple_spring_restful_api.repository.CustomerRepository;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private Validator validator;
	
	@Transactional
	public CustomerResponse createCustomer(CreateCustomerRequest customerRequest) {
		Set<ConstraintViolation<CreateCustomerRequest>> constraintViolations = validator.validate(customerRequest);

		if(!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
		 
		Customer customer = new Customer();	
		customer.setPhoneNumber(customerRequest.getPhoneNumber());
	    customer.setEmail(customerRequest.getEmail());
	    customer.setAddress(customerRequest.getAddress());
		customer.setAccounts(customer.getAccounts());

	    customerRepository.save(customer);

		return toCustomerResponse(customer);
	}

	private CustomerResponse toCustomerResponse(Customer customer) {
		List<AccountResponse> accountResponses = customer.getAccounts().stream()
				.map(account -> AccountResponse.builder()
						.accountNumber(account.getAccountNumber())
						.accountType(account.getAccountType())
						.accountName(account.getAccountName())
						.accountBalance(account.getAccountBalance())
						.build())
				.toList();

		return CustomerResponse.builder()
				.id(customer.getId())
				.phoneNumber(customer.getPhoneNumber())
				.email(customer.getEmail())
				.address(customer.getAddress())
				.account(accountResponses)
				.build();
	}

	@Transactional(readOnly = true)
	public CustomerResponse getCustomer(String id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

		return toCustomerResponse(customer);
	}
	
	@Transactional
	public CustomerResponse updateCustomer(UpdateCustomerRequest updateCustomerRequest) {
		Customer customer = customerRepository.findById(updateCustomerRequest.getId())
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

		if (Objects.nonNull(updateCustomerRequest.getAddress())) {
			customer.setAddress(updateCustomerRequest.getAddress());
		}

		if (Objects.nonNull(updateCustomerRequest.getPhoneNumber())) {
			customer.setPhoneNumber(updateCustomerRequest.getPhoneNumber());
		}

		if (Objects.nonNull(updateCustomerRequest.getEmail())) {
			customer.setEmail(updateCustomerRequest.getEmail());
		}

		customerRepository.save(customer);

		return toCustomerResponse(customer);
	}

	@Transactional
	public void deleteCustomer(String id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
		customerRepository.delete(customer);
	}

	@Transactional(readOnly = true)
	public Page<CustomerResponse> getAllCustomers(PagingCustomerRequest pagingCustomerRequest) {
		Pageable pageable = PageRequest.of(pagingCustomerRequest.getPage(), pagingCustomerRequest.getSize());
		Page<Customer> customers = customerRepository.findAll(pageable);
		List<CustomerResponse> customerResponses = customers.getContent().stream()
				.map(this::toCustomerResponse)
				.toList();
		return  new PageImpl<>(customerResponses, pageable, customers.getTotalElements());
	}
}
