package com.leaseleap.userservice.config;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private static PasswordEncoder passwordEncoder;
	
    public static PasswordEncoder getPasswordEncoder() {
    	if(Objects.isNull(passwordEncoder)) {
    		passwordEncoder = new BCryptPasswordEncoder();
    	}
        return passwordEncoder;
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> 
				authorize.anyRequest().permitAll())		
			.csrf(csrf -> csrf.disable());		
		return http.build();		
	}

}
