package com.leaseleap.userservice.service;

import java.util.List;

import com.leaseleap.userservice.model.PatchUserRequest;
import com.leaseleap.userservice.model.Users;


public interface UserService {
	
	public void createUser(Users user);

	public List<Users> listUsers();

	public Users validateUser(Long userId);
	
	public Users updateUser(Long userId, PatchUserRequest patchUserRequest);
	
	public void deleteUser(Long userId);


}
