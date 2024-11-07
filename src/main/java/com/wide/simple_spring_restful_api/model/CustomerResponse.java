package com.wide.simple_spring_restful_api.model;

import com.wide.simple_spring_restful_api.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private int id;
    private String phoneNumber;
    private String email;
    private String address;
    private List<AccountResponse> account;
}
