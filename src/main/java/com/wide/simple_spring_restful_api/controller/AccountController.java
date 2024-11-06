package com.wide.simple_spring_restful_api.controller;

import com.wide.simple_spring_restful_api.model.WebResponse;
import com.wide.simple_spring_restful_api.model.AccountResponse;
import com.wide.simple_spring_restful_api.model.dto.CreateAccountRequest;
import com.wide.simple_spring_restful_api.model.dto.UpdateAccountRequest;
import com.wide.simple_spring_restful_api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping(
            path = "/api/customers/{customerId}/accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AccountResponse> createAccount(@RequestBody CreateAccountRequest createAccountRequest, @PathVariable("customerId") int customerId) {
        createAccountRequest.setCustomerId(customerId);
        AccountResponse accountResponse = accountService.createAccount(createAccountRequest);
        return WebResponse.<AccountResponse>builder().data(accountResponse).build();
    }

    @GetMapping(
            path = "/api/customers/{customerId}/accounts/{accountNumber}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AccountResponse> getAccount(@PathVariable("customerId") int customerId,
                                                   @PathVariable("accountNumber") String accountNumber) {
        AccountResponse accountByAccountNumber = accountService.getAccountByAccountNumber(customerId, accountNumber);
        return WebResponse.<AccountResponse>builder().data(accountByAccountNumber).build();
    }

    @PutMapping(
            path = "/api/customers/{customerId}/accounts/{accountNumber}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AccountResponse> updateAccount(@RequestBody UpdateAccountRequest request,
                                                      @PathVariable("customerId") int customerId,
                                                      @PathVariable("accountNumber") String accountNumber) {
        request.setCustomerId(customerId);
        request.setAccountNumber(accountNumber);

        AccountResponse accountResponse = accountService.updateAccount(request, accountNumber);
        return WebResponse.<AccountResponse>builder().data(accountResponse).build();
    }

    @DeleteMapping(
            path = "/api/customers/{customerId}/accounts/{accountNumber}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteAccount(@PathVariable("customerId") int customerId,
                                                      @PathVariable("accountNumber") String accountNumber) {
        accountService.deleteAccount(customerId, accountNumber);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/customers/{customerId}/accounts",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AccountResponse>> listAccount(@PathVariable("customerId") int customerId) {
        List<AccountResponse> accountResponses = accountService.getAllAccounts(customerId);
        return WebResponse.<List<AccountResponse>>builder().data(accountResponses).build();
    }
}
