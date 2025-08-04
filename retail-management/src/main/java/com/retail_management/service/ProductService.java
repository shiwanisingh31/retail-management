package com.retail_management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail_management.dao.ProductsDAO;
import com.retail_management.entity.Product;

@Service
public class ProductService {

    @Autowired
    ProductsDAO productDao;

    public String saveProductsToDB(String name, String category, int price, int stock) {
        try {
            // Validation
            if (name == null || name.trim().isEmpty()) {
                return "error: Product name is required";
            }
            if (category == null || category.trim().isEmpty()) {
                return "error: Product category is required";
            }
            if (price <= 0) {
                return "error: Price must be positive";
            }
            if (stock < 0) {
                return "error: Stock cannot be negative";
            }

            Product entity = new Product();
            entity.setName(name.trim());
            entity.setCategory(category.trim());
            entity.setPrice(price);
            entity.setStock(stock);

            productDao.saveAndFlush(entity);
            return "success";
        } catch (Exception e) {
            System.err.println("Error saving product: " + e.getMessage());
            return "error: Failed to save product - " + e.getMessage();
        }
    }

    public List<Product> listProducts() {
        try {
            return productDao.findAll();
        } catch (Exception e) {
            System.err.println("Error listing products: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public String delProducts(int id) {
        try {
            if (id <= 0) {
                return "error: Invalid product ID";
            }
            
            if (!productDao.existsById(id)) {
                return "error: Product not found";
            }
            
            productDao.deleteById(id);
            return "success";
        } catch (Exception e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return "error: Failed to delete product - " + e.getMessage();
        }
    }
    
    public List<Product> searchProducts(String productName) {
        try {
            if (productName == null || productName.trim().isEmpty()) {
                return listProducts(); // Return all products if search term is empty
            }
            
            List<Product> collectDataToSearch = listProducts();
            List<Product> productFound = new ArrayList<Product>();
            
            String searchTerm = productName.toLowerCase().trim();
            
            for (Product searchedProduct : collectDataToSearch) {
                if (searchedProduct.getName() != null && 
                    searchedProduct.getName().toLowerCase().contains(searchTerm)) {
                    productFound.add(searchedProduct);
                }
            }
            return productFound;
        } catch (Exception e) {
            System.err.println("Error searching products: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}