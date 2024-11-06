package com.wide.simple_spring_restful_api.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    @NotBlank
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
