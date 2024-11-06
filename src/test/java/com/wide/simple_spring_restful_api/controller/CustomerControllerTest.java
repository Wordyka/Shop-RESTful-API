package com.wide.simple_spring_restful_api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wide.simple_spring_restful_api.entity.Customer;
import com.wide.simple_spring_restful_api.model.CustomerResponse;
import com.wide.simple_spring_restful_api.model.WebResponse;
import com.wide.simple_spring_restful_api.model.dto.CreateCustomerRequest;
import com.wide.simple_spring_restful_api.model.dto.UpdateCustomerRequest;
import com.wide.simple_spring_restful_api.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void testCreateCustomerSuccess() throws Exception {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setPhoneNumber("1314322324");
        createCustomerRequest.setAddress("Jakarta");
        createCustomerRequest.setEmail("customer@gmail.com");

        mockMvc.perform(
                post("/api/customers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCustomerRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });

            assertNull(response.getErrors());
            assertEquals("1314322324", response.getData().getPhoneNumber());
            assertEquals("customer@gmail.com", response.getData().getEmail());
            assertEquals("Jakarta",response.getData().getAddress());
            assertTrue(customerRepository.existsById(String.valueOf(response.getData().getId())));
        });
    }

    @Test
    void testCreateCustomerFailed() throws Exception {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setPhoneNumber("");
        createCustomerRequest.setAddress("");
        createCustomerRequest.setEmail("");

        mockMvc.perform(
                post("/api/customers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCustomerRequest))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>(){ });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetCustomerByIdSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setPhoneNumber("1314322324");
        customer.setAddress("Jakarta");
        customer.setEmail("customer@gmail.com");
        customerRepository.save(customer);

        mockMvc.perform(
                get("/api/customers/"+customer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNull(response.getErrors());

            assertEquals("1314322324", response.getData().getPhoneNumber());
            assertEquals("customer@gmail.com", response.getData().getEmail());
            assertEquals("Jakarta",response.getData().getAddress());
        });
    }

    @Test
    void testGetCustomerByIdFailed() throws Exception {
        mockMvc.perform(
                get("/api/customers/131312")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>(){ });

            assertNotNull(response.getErrors());
        });
    }


    @Test
    void testUpdateCustomerSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setPhoneNumber("1314322324");
        customer.setAddress("Jakarta");
        customer.setEmail("customer@gmail.com");
        customerRepository.save(customer);

        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest();
        updateCustomerRequest.setPhoneNumber("567567567");
        updateCustomerRequest.setAddress("Bandung");
        updateCustomerRequest.setEmail("customer2@gmail.com");

        mockMvc.perform(
                put("/api/customers/"+customer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });

            assertNull(response.getErrors());
            assertEquals("567567567", response.getData().getPhoneNumber());
            assertEquals("customer2@gmail.com", response.getData().getEmail());
            assertEquals("Bandung",response.getData().getAddress());
            assertTrue(customerRepository.existsById(String.valueOf(response.getData().getId())));
        });
    }

    @Test
    void testUpdateCustomerFailed() throws Exception {
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest();
        updateCustomerRequest.setPhoneNumber("13131123");
        updateCustomerRequest.setAddress("");
        updateCustomerRequest.setEmail("customer");

        mockMvc.perform(
                put("/api/customers/4")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerRequest))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>(){ });

            assertNotNull(response.getErrors());
        });
    }


    @Test
    void testDeleteCustomerSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setPhoneNumber("1314322324");
        customer.setAddress("Jakarta");
        customer.setEmail("customer@gmail.com");
        customerRepository.save(customer);

        mockMvc.perform(
                delete("/api/customers/"+customer.getId())
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
    void testDeleteCustomerFailed() throws Exception {
        mockMvc.perform(
                delete("/api/customers/131312")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>(){ });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetAllCustomersSuccess() throws Exception {
        for(int i=0; i<20; i++) {
            Customer customer = new Customer();
            customer.setPhoneNumber("1314322324");
            customer.setAddress("Jakarta");
            customer.setEmail("customer@gmail.com");
            customerRepository.save(customer);
        }

        mockMvc.perform(
                get("/api/customers")
                        .queryParam("page", "1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<CustomerResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){ });
            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(2, response.getPagingResponse().getTotalPages());
            assertEquals(1, response.getPagingResponse().getCurrentPage());
            assertEquals(10, response.getPagingResponse().getSize());
        });
    }



}