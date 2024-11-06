package com.wide.simple_spring_restful_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="accounts")
public class Account {
	@Id
	private String accountNumber;
	
	private String accountType;
	
	private String accountName;
	
	private double accountBalance;
	
	@ManyToOne	
	@JoinColumn(name="customer_id", referencedColumnName ="id" )
	private Customer customer;
}
