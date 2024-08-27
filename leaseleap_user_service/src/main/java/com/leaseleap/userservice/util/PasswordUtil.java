package com.leaseleap.userservice.util;


import org.springframework.security.crypto.password.PasswordEncoder;

import com.leaseleap.userservice.config.SecurityConfig;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class PasswordUtil {
	
	
	private static PasswordEncoder passwordEncoder = SecurityConfig.getPasswordEncoder();
	
	public static String encode(String password) {
		return passwordEncoder.encode(password);		
	}
	
	public static boolean matches(String password, String savedPassword) {
		return passwordEncoder.matches(password, savedPassword);
	}
}
