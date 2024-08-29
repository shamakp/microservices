package com.leaseleap.userservice.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.leaseleap.userservice.exception.InvalidUserIdException;
import com.leaseleap.userservice.exception.UserCreationException;
import com.leaseleap.userservice.exception.UserNotFoundException;
import com.leaseleap.userservice.exception.UserUpdateFailedException;
import com.leaseleap.userservice.model.LoginUserRequest;
import com.leaseleap.userservice.model.PatchUserRequest;
import com.leaseleap.userservice.model.Users;
import com.leaseleap.userservice.repository.UserRepository;
import com.leaseleap.userservice.util.PasswordUtil;
import com.leaseleap.userservice.util.UserConstants;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepo;
	
	public Users createUser(Users user) {
		log.debug("Entering createUser method");;
		String encodedPassword = PasswordUtil.encode(user.getPassword());
		user.setPassword(encodedPassword);
		Users savedUser = userRepo.save(user);
		if(Objects.isNull(savedUser)) {
			UserCreationException userCreationException = 
					new UserCreationException("Cannot create the user with username: "+user.getUsername());
			log.error("UserCreationException", userCreationException);
			throw userCreationException;
		}
		log.debug("Exiting createUser method");
		return savedUser;
	}

	public List<Users> listUsers() {
		log.debug("Entering listUsers method");
		List<Users> userList = userRepo.findAll();
		if(Objects.isNull(userList) || userList.isEmpty()) {
			UserNotFoundException userNotFoundException = new UserNotFoundException("No users found");
			log.error("UserNotFoundException",userNotFoundException);
			throw userNotFoundException;
		}
		log.debug("Exiting listUsers method");
		return userList;
	}

	public Users validateUser(Long userId) {
		log.debug("Entering validateUser method");
		Users validatedUser = userRepo.findById(userId).orElse(null);
		if(Objects.isNull(validatedUser)) {
			UserNotFoundException userNotFoundException = 
					new UserNotFoundException("Cannot find the user with user ID: "+userId);
			log.error("UserNotFoundException", userNotFoundException);
			throw userNotFoundException;
		}
		log.debug("Exiting validateUser method");
		return validatedUser;	
	}
	
	public Users validateUserByUsername(String username) {
		log.debug("Entering validateUserByUsername method");
		Users validatedUser = userRepo.findByUsername(username);
		if(Objects.isNull(validatedUser)) {
			UserNotFoundException userNotFoundException = 
					new UserNotFoundException("Cannot find a user with username: "+username);
			log.error("UserNotFoundException", userNotFoundException);
			throw userNotFoundException;
		}
		log.debug("Exiting validateUserByUsername method");
		return validatedUser;
	}
	
	public Users updateUser(Long userId, PatchUserRequest patchUserRequest) {
		log.debug("Entering updateUser method");
		Users user = userRepo.findById(userId).orElseThrow();
		user = updateDetails(user, patchUserRequest);
		if(Objects.isNull(user)) {
			UserUpdateFailedException userUpdateFailedException = 
					new UserUpdateFailedException("Update failed for the user with user ID: "+ userId);
			log.error("UserUpdateFailedException", userUpdateFailedException);
			throw userUpdateFailedException;
		}
		log.debug("Exiting updateUser method");
		return user;
	}	

	public void deleteUser(Long userId) {
		log.debug("Entering deleteUser method");
		if(userId <= 0L) {
			InvalidUserIdException invalidUserIdException =
					new InvalidUserIdException("Invalid User ID: "+userId);
			log.error("InvalidUserIdException", invalidUserIdException);
			throw invalidUserIdException;
		}
		userRepo.deleteById(userId);
		log.debug("Exiting deleteUser method");
	}
	
	private Users updateDetails(Users user, PatchUserRequest patchUserRequest) {
		log.debug("Entering updateDetails method");
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getFirstName())) {
			user.setFirstName(patchUserRequest.getFirstName());
			log.info("Username: "+user.getUsername()
				+"\tUpdating firstname to "+patchUserRequest.getFirstName());
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getLastName())) {
			user.setLastName(patchUserRequest.getLastName());
			log.info("Username: "+user.getUsername()
			+"\tUpdating lastname to "+patchUserRequest.getLastName());
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getPassword())) {
			user.setPassword(PasswordUtil.encode(patchUserRequest.getPassword()));
			log.info("Username: "+user.getUsername()+"\tUpdating password");
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getEmail())) {
			user.setEmail(patchUserRequest.getEmail());
			log.info("Username: "+user.getUsername()+"\tUpdating Email");
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getMobileNumber())) {
			user.setMobileNumber(patchUserRequest.getMobileNumber());
			log.info("Username: "+user.getUsername()+"\tUpdating MobileNumber");
		}
		log.debug("Exiting updateDetails method");
		return userRepo.save(user);
	}

	public boolean login(LoginUserRequest loginUser) {
		log.debug("Entering login method");
		Users validatedUser = validateUserByUsername(loginUser.getUsername());
		log.debug("User validated");
		if(Objects.nonNull(validatedUser)) {
			log.debug("Checking password and exiting login method");
			return PasswordUtil.matches(loginUser.getPassword(), validatedUser.getPassword());			
		} else {
			UsernameNotFoundException usernameNotFoundException = 
					new UsernameNotFoundException("Cannot find a user with username: "+loginUser.getUsername());
			log.error("UsernameNotFoundException", usernameNotFoundException);
			throw usernameNotFoundException;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Entering loadUserByUsername method");
		Users user = userRepo.findByUsername(username);
		if(Objects.isNull(user)) {
			UsernameNotFoundException usernameNotFoundException =
					new UsernameNotFoundException("Cannot find the username: "+username);
			log.error("UsernameNotFoundException", usernameNotFoundException);
			throw usernameNotFoundException;
		}
		log.debug("Exiting loadUserByUsername method");
		return new User(user.getUsername(),
				user.getPassword(), Collections.singletonList(
						new SimpleGrantedAuthority(UserConstants.USER)));
	}	

}
