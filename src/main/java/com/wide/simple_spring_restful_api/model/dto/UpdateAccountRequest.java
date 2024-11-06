package com.wide.simple_spring_restful_api.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountRequest {
    @NotBlank
    @JsonIgnore
    private String accountNumber;

    @NotBlank
    private String accountType;

    @NotBlank
    private String accountName;

    @NotNull
    private Double accountBalance;

    @JsonIgnore
    @Column(name="customer_id")
    @NotNull
    private int customerId;
}
