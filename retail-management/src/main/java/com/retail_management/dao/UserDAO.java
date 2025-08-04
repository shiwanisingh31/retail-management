package com.retail_management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.retail_management.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User, Integer>{

	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.phoneno = :phone AND u.password = :password")
	boolean authentication(@Param("phone") int phone, @Param("password") String password);
}
