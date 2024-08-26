package com.leaseleap.userservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class LoginUserRequest {
	
	private String username;
		
	private String password;
	
}
