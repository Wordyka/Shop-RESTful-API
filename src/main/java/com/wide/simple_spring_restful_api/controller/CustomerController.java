package com.wide.simple_spring_restful_api.controller;


import com.wide.simple_spring_restful_api.model.CustomerResponse;
import com.wide.simple_spring_restful_api.model.PagingResponse;
import com.wide.simple_spring_restful_api.model.dto.PagingCustomerRequest;
import com.wide.simple_spring_restful_api.model.dto.UpdateCustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.wide.simple_spring_restful_api.model.WebResponse;
import com.wide.simple_spring_restful_api.model.dto.CreateCustomerRequest;
import com.wide.simple_spring_restful_api.service.CustomerService;

import java.util.List;

@RestController
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@PostMapping(
			path = "/api/customers", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<CustomerResponse> createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
		CustomerResponse customerResponse = customerService.createCustomer(createCustomerRequest);
		return WebResponse.<CustomerResponse>builder().data(customerResponse).build();
	}

	@GetMapping(
			path = "/api/customers/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<CustomerResponse> getCustomerById(@PathVariable("id") String id) {
		CustomerResponse customerResponse = customerService.getCustomer(id);
		return WebResponse.<CustomerResponse>builder().data(customerResponse).build();
	}

	@PutMapping (
			path = "/api/customers/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<CustomerResponse> updateCustomer(@RequestBody UpdateCustomerRequest updateCustomerRequest, @PathVariable("id") String id) {
		updateCustomerRequest.setId(id);
		CustomerResponse customerResponse = customerService.updateCustomer(updateCustomerRequest);
		return WebResponse.<CustomerResponse>builder().data(customerResponse).build();
	}

	@DeleteMapping(
			path = "/api/customers/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<String> deleteCustomer(@PathVariable("id") String id) {
		customerService.deleteCustomer(id);
		return WebResponse.<String>builder().data("OK").build();
	}

	@GetMapping(
			path = "/api/customers",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<List<CustomerResponse>> getAllCustomer(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
															  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
		PagingCustomerRequest request = PagingCustomerRequest.builder()
				.page(page)
				.size(size)
				.build();

		Page<CustomerResponse> allCustomers = customerService.getAllCustomers(request);
		return WebResponse.<List<CustomerResponse>>builder()
				.data(allCustomers.getContent())
				.pagingResponse(PagingResponse.builder()
						.currentPage(allCustomers.getNumber())
						.totalPages(allCustomers.getTotalPages())
						.size(allCustomers.getSize())
						.build())
				.build();
	}
}
