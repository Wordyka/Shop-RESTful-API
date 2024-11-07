package com.wide.simple_spring_restful_api.service;

import com.wide.simple_spring_restful_api.entity.Account;
import com.wide.simple_spring_restful_api.entity.Customer;
import com.wide.simple_spring_restful_api.model.AccountResponse;
import com.wide.simple_spring_restful_api.model.dto.CreateAccountRequest;
import com.wide.simple_spring_restful_api.model.dto.UpdateAccountRequest;
import com.wide.simple_spring_restful_api.repository.AccountRepository;
import com.wide.simple_spring_restful_api.repository.CustomerRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class AccountService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Validator validator;

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        log.info("Received CreateAccountRequest: {}", createAccountRequest);

        Set<ConstraintViolation<CreateAccountRequest>> constraintViolations = validator.validate(createAccountRequest);
        if(!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        Customer customer = customerRepository.findById(createAccountRequest.getCustomerId())
                .orElseThrow(() -> {
                    log.error("Customer with ID {} not found", createAccountRequest.getCustomerId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
                });

        Account account = new Account();
        account.setAccountNumber(createAccountRequest.getAccountNumber());
        account.setAccountType(createAccountRequest.getAccountType());
        account.setAccountName(createAccountRequest.getAccountName());
        account.setAccountBalance(createAccountRequest.getAccountBalance());
        account.setCustomer(customer);

        log.info("Saving Account: {}", account);
        accountRepository.save(account);

        AccountResponse accountResponse = toAccountResponse(account);
        log.info("Created AccountResponse: {}", accountResponse);

        return accountResponse;
    }

    private AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .accountName(account.getAccountName())
                .accountBalance(account.getAccountBalance())
                .build();
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountByAccountNumber(int id, String accountNumber) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Account account = accountRepository.findByCustomerIdAndAccountNumber(id, accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        return toAccountResponse(account);

    }

    @Transactional
    public AccountResponse updateAccount(UpdateAccountRequest updateAccountRequest, String originalAccountNumber) {
        log.info("Starting updateAccount with originalAccountNumber: {} and newAccountNumber: {}", originalAccountNumber, updateAccountRequest.getAccountNumber());

        Set<ConstraintViolation<UpdateAccountRequest>> constraintViolations = validator.validate(updateAccountRequest);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        Customer customer = customerRepository.findById(updateAccountRequest.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Account account = accountRepository.findByCustomerIdAndAccountNumber(updateAccountRequest.getCustomerId(), originalAccountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        log.info("Account found: {}", account.getAccountNumber());

        // Log the existing account number before updating
        log.info("Existing Account Number before update: {}", account.getAccountNumber());

        // Update the account fields, including the account number
        account.setAccountNumber(originalAccountNumber);
        if (Objects.nonNull(updateAccountRequest.getAccountType())) {
            account.setAccountType(updateAccountRequest.getAccountType());
        }

        if (Objects.nonNull(updateAccountRequest.getAccountName())) {
            account.setAccountName(updateAccountRequest.getAccountName());
        }

        if (Objects.nonNull(updateAccountRequest.getAccountBalance())) {
            account.setAccountBalance(updateAccountRequest.getAccountBalance());
        }

        account.setCustomer(customer);


        // Log the updated account number
        log.info("Updated Account Number after set: {}", account.getAccountNumber());

        // Save the updated account
        accountRepository.save(account);

        log.info("Account saved with new account number: {}", account.getAccountNumber());

        return toAccountResponse(account);
    }

    @Transactional
    public void deleteAccount(int id, String accountNumber) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Account account = accountRepository.findByCustomerIdAndAccountNumber(id, accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        accountRepository.delete(account);

    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        List<Account> accounts = accountRepository.findAllByCustomer(customer);
        return accounts.stream().map(this::toAccountResponse).toList();
    }
}
