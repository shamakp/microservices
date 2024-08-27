package com.leaseleap.userservice.controller;


import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.leaseleap.userservice.exception.WrongPasswordException;
import com.leaseleap.userservice.model.LoginUserRequest;
import com.leaseleap.userservice.model.PatchUserRequest;
import com.leaseleap.userservice.model.Users;
import com.leaseleap.userservice.service.UserService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("leaseleap/v1")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public void login(@Valid @RequestBody LoginUserRequest loginUser) {
		if(!userService.login(loginUser)) {
			throw new WrongPasswordException("The given password is wrong");
		}
		return;
	}
	
	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public Users createUser(@Valid @RequestBody Users user) {
		return userService.createUser(user);
	}
	
	@GetMapping("/users")
	@ResponseStatus(HttpStatus.OK)
	public List<Users> listUsers() {
		return userService.listUsers();
	}
	
	@PostMapping("/{user_id}")
	@ResponseStatus(HttpStatus.OK)
	public Users validateuser(@PathVariable("user_id") String userId) {
		Users user = Optional.ofNullable(userId)
				.map(Long::valueOf)
				.map(userService::validateUser)
				.orElseThrow();
		return user;
	}
	
	@PatchMapping("/{user_id}")
	@ResponseStatus(HttpStatus.OK)
	public Users updateUser(@PathVariable("user_id") String userId, 
			@RequestBody PatchUserRequest patchUserReq) {
		Users user = Optional.ofNullable(userId)
				.map(Long::valueOf)
				.map(id -> userService.updateUser(id, patchUserReq))
				.orElseThrow();
		return user;
	}
	
	@DeleteMapping("/{user_id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable("user_id") String userId) {
		userService.deleteUser(Optional.ofNullable(userId)
				.map(Long::valueOf).orElse(0L));				
	}	

}
