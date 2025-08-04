package com.retail_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail_management.dao.CustomersDAO;

import com.retail_management.entity.Customer;


@Service
public class CustomerService {
	@Autowired
	CustomersDAO customerDao;

	public String saveCustomersToDB(

			String name,
			int phoneno,
			String email,
			int age,
			String gender) 
	{
	Customer entity = new Customer();

		entity.setName(name);
		entity.setEmail(email);
		entity.setAge(age);
		entity.setGender(gender);
		entity.setPhoneno(phoneno);
	

		customerDao.saveAndFlush(entity);

		return "success";
	}

	public List<Customer> listCustomers() {

		return customerDao.findAll();

	}
	
	public String delCustomer(int id) {
		
			customerDao.deleteById(id);
			return "success";
	}

}


