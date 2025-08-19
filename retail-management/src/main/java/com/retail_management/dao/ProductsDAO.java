package com.retail_management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retail_management.entity.Product;

@Repository
public interface ProductsDAO  extends JpaRepository<Product, Long>{ 
	
	

}
