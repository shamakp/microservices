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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("leaseleap/v1")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public void login(@Valid @RequestBody LoginUserRequest loginUser) {
		log.debug("Entering login method");
		if(!userService.login(loginUser)) {
			WrongPasswordException wrongPasswordException = 
					new WrongPasswordException("The given password is wrong");
			log.error("WrongPasswordException", wrongPasswordException);
			throw wrongPasswordException;
		}
		log.debug("Exiting login method");
		return;
	}
	
	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public Users createUser(@Valid @RequestBody Users user) {
		log.debug("Entering user creation method");
		Users createdUser = userService.createUser(user);
		log.debug("Exiting user creation method");
		return createdUser;
	}
	
	@GetMapping("/users")
	@ResponseStatus(HttpStatus.OK)
	public List<Users> listUsers() {
		log.debug("Entering listUser method");
		List<Users> userList = userService.listUsers(); 
		log.debug("Exiting listUser method");
		return userList;
	}
	
	@PostMapping("/{user_id}")
	@ResponseStatus(HttpStatus.OK)
	public Users validateuser(@PathVariable("user_id") String userId) {
		log.debug("Entering validate user method");
		Users user = Optional.ofNullable(userId)
				.map(Long::valueOf)
				.map(userService::validateUser)
				.orElseThrow();
		log.debug("Exiting validate user method");
		return user;
	}
	
	@PatchMapping("/{user_id}")
	@ResponseStatus(HttpStatus.OK)
	public Users updateUser(@PathVariable("user_id") String userId, 
			@RequestBody PatchUserRequest patchUserReq) {
		log.debug("Entering update user method");
		Users user = Optional.ofNullable(userId)
				.map(Long::valueOf)
				.map(id -> userService.updateUser(id, patchUserReq))
				.orElseThrow();
		log.debug("Exiting update user method");
		return user;
	}
	
	@DeleteMapping("/{user_id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable("user_id") String userId) {
		log.debug("Entering delete user method");
		userService.deleteUser(Optional.ofNullable(userId)
				.map(Long::valueOf).orElse(0L));
		log.debug("Exiting delete user method");
	}	

}
