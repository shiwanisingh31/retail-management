package com.retail_management.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail_management.entity.Product;
import com.retail_management.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductRestController {

	@Autowired
	ProductService productServ;

	@PostMapping("/save/products")
	public String saveProducts(@RequestBody Product product) {
	    System.out.println(product.getName() + " " + product.getCategory() + " " + product.getStock() + " " + product.getPrice());
	    return productServ.saveProductsToDB(
	        product.getName(),
	        product.getCategory(),
	        product.getPrice(),
	        product.getStock()
	    );
	}
	
	
	@GetMapping("/list/products")
	public List<Product> listProducts() {

		return productServ.listProducts();

	}
	
	@GetMapping ("/delete/products")
	public String delProduct(@RequestParam("id") Long id) {
		return productServ.delProducts(id);
		
		
	}
    @GetMapping("/search/products")	
	public List<Product> searchProducts(@RequestParam("productName")String productName) {
		return productServ.searchProducts(productName);
	}

}
