package com.wide.simple_spring_restful_api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wide.simple_spring_restful_api.entity.Account;
import com.wide.simple_spring_restful_api.entity.Customer;
import com.wide.simple_spring_restful_api.model.AccountResponse;
import com.wide.simple_spring_restful_api.model.WebResponse;
import com.wide.simple_spring_restful_api.model.dto.CreateAccountRequest;
import com.wide.simple_spring_restful_api.model.dto.UpdateAccountRequest;
import com.wide.simple_spring_restful_api.repository.AccountRepository;
import com.wide.simple_spring_restful_api.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void createAccountSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setPhoneNumber("1314322324");
        customer.setAddress("Jakarta");
        customer.setEmail("customer2@gmail.com");
        customerRepository.save(customer);

        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setAccountNumber("1313141");
        createAccountRequest.setAccountType("Savings");
        createAccountRequest.setAccountName("Savings Account");
        createAccountRequest.setAccountBalance(5000.00);
        createAccountRequest.setCustomerId(customer.getId());

        mockMvc.perform(
                post("/api/customers/"+customer.getId()+"/accounts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AccountResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNull(response.getErrors());
            assertEquals(createAccountRequest.getAccountNumber(), response.getData().getAccountNumber());
            assertEquals(createAccountRequest.getAccountType(), response.getData().getAccountType());
            assertEquals(createAccountRequest.getAccountName(), response.getData().getAccountName());
            assertEquals(createAccountRequest.getAccountBalance(), response.getData().getAccountBalance());
        });
    }

    @Test
    void createAccountFailed() throws Exception {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setAccountName("");

        mockMvc.perform(
                post("/api/customers/10/accounts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountRequest))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getAccountSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setPhoneNumber("1314322324");
        customer.setAddress("Jakarta");
        customer.setEmail("customer2@gmail.com");
        customerRepository.save(customer);

        Account account = new Account();
        account.setAccountNumber("1313141");
        account.setAccountType("Savings");
        account.setAccountName("Savings Account");
        account.setAccountBalance(5000.00);
        account.setCustomer(customer);
        accountRepository.save(account);

        mockMvc.perform(
                get("/api/customers/"+customer.getId()+"/accounts/1313141")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AccountResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNull(response.getErrors());
            assertEquals(account.getAccountNumber(), response.getData().getAccountNumber());
            assertEquals(account.getAccountType(), response.getData().getAccountType());
            assertEquals(account.getAccountName(), response.getData().getAccountName());
            assertEquals(account.getAccountBalance(), response.getData().getAccountBalance());
        });
    }


    @Test
    void getAccountFailed() throws Exception {
        mockMvc.perform(
                get("/api/customers/1121/accounts/32414")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void updateAccountSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setPhoneNumber("1314322324");
        customer.setAddress("Jakarta");
        customer.setEmail("customer2@gmail.com");
        customerRepository.save(customer);

        Account account = new Account();
        account.setAccountNumber("1313141");
        account.setAccountType("Savings");
        account.setAccountName("Savings Account");
        account.setAccountBalance(5000.00);
        account.setCustomer(customer);
        accountRepository.save(account);

        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setAccountType("Savings");
        updateAccountRequest.setAccountName("Savings Account");
        updateAccountRequest.setAccountBalance(4560.00);
        updateAccountRequest.setAccountNumber("9999");


        mockMvc.perform(
                put("/api/customers/"+customer.getId()+"/accounts/1313141")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAccountRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AccountResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNull(response.getErrors());
            assertEquals(updateAccountRequest.getAccountType(), response.getData().getAccountType());
            assertEquals(updateAccountRequest.getAccountName(), response.getData().getAccountName());
            assertEquals(updateAccountRequest.getAccountBalance(), response.getData().getAccountBalance());
            assertEquals(updateAccountRequest.getCustomerId(), customer.getId());

        });
    }

    @Test
    void updateAccountFailed() throws Exception {
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setAccountName("");

        mockMvc.perform(
                put("/api/customers/10/accounts/1231")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAccountRequest))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void deleteAccountFailed() throws Exception {
        mockMvc.perform(
                delete("/api/customers/1121/accounts/32414")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void deleteAccountSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setPhoneNumber("1314322324");
        customer.setAddress("Jakarta");
        customer.setEmail("customer2@gmail.com");
        customerRepository.save(customer);

        Account account = new Account();
        account.setAccountNumber("1313141");
        account.setAccountType("Savings");
        account.setAccountName("Savings Account");
        account.setAccountBalance(5000.00);
        account.setCustomer(customer);
        accountRepository.save(account);

        mockMvc.perform(
                delete("/api/customers/"+customer.getId()+"/accounts/1313141")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNull(response.getErrors());
            assertEquals("OK", response.getData());
        });
    }


    @Test
    void listAccountSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setPhoneNumber("1314322324");
        customer.setAddress("Jakarta");
        customer.setEmail("customer2@gmail.com");
        customerRepository.save(customer);

        for (int i = 0; i < 5; i++) {
            Account account = new Account();
            account.setAccountNumber("1313141"+i);
            account.setAccountType("Savings");
            account.setAccountName("Savings Account");
            account.setAccountBalance(5000.00);
            account.setCustomer(customer);
            accountRepository.save(account);
        }

        mockMvc.perform(
                get("/api/customers/"+customer.getId()+"/accounts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<AccountResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNull(response.getErrors());
            assertEquals(5, response.getData().size());

        });
    }


    @Test
    void listAccountFailed() throws Exception {
        mockMvc.perform(
                get("/api/customers/1121/accounts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNotNull(response.getErrors());
        });
    }


}