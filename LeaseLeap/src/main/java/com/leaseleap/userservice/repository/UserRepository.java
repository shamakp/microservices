package com.leaseleap.userservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.leaseleap.userservice.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>{
	
}
