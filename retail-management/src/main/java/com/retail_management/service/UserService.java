package com.retail_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail_management.dao.UserDAO;
import com.retail_management.entity.User;


@Service
public class UserService {

	@Autowired
	UserDAO userDao;

	public String saveUserToDB(
			String name,
			long phoneno,
			String password
			) 
	{
		User entity = new User();

		entity.setName(name);
		entity.setPassword(password);
		entity.setPhoneno(phoneno);
		
		userDao.saveAndFlush(entity);

		return "success";
	}

	public List<User> listUser() {

		return userDao.findAll();

	}
	public String delUser(int id){
		userDao.deleteById(id);
		return "success";
		
	}

	public boolean authenticate(long phone, String password) {
	    return userDao.authentication(phone, password);
	}
	
}



