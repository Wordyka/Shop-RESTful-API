package com.wide.simple_spring_restful_api.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCustomerRequest {
    @NotBlank
    @Size(max = 15)
    private String phoneNumber;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String address;

}
