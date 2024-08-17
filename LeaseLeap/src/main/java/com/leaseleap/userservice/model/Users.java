package com.leaseleap.userservice.model;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty("user_id")
	private Long userId;
	
	@NotBlank
	@UniqueElements
	private String username;
	
	@NotBlank
	private String password;
	
	@JsonProperty("first_name")
	@NotBlank
	private String firstName;
	
	@JsonProperty("last_name")
	private String lastName;
	
	@JsonProperty("is_admin")
	@NotNull
	private boolean isAdmin;
	
	@JsonProperty("mobile_number")
	@NotBlank
	private String mobileNumber;
	
	@Email
	private String email;

}
