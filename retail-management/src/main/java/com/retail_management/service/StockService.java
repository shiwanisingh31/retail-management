package com.retail_management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail_management.dao.StockRepository;
import com.retail_management.entity.StockTracking;

@Service
public class StockService {
    
    @Autowired
    private StockRepository stockRepository;
    
    // Add new stock entry
    public String addStock(StockTracking stock) {
        try {
            // Validation
            if (stock == null) {
                return "error: Stock data is required";
            }
            if (stock.getProduct() == null) {
                return "error: Product is required";
            }
            if (stock.getProduct().getId() <= 0) {
                return "error: Valid product ID is required";
            }
            if (stock.getProduct().getName() == null || stock.getProduct().getName().trim().isEmpty()) {
                return "error: Product name is required";
            }
            if (stock.getCurrentStock() < 0) {
                return "error: Stock quantity cannot be negative";
            }
            if (stock.getMinimumStock() < 0) {
                return "error: Minimum stock level cannot be negative";
            }
            
            stock.setLastUpdated(LocalDateTime.now());
            stockRepository.save(stock);
            return "success";
        } catch (Exception e) {
            System.err.println("Error adding stock: " + e.getMessage());
            return "error: Failed to add stock - " + e.getMessage();
        }
    }
    
    // Update stock quantity
    public String updateStock(Long productId, int quantity) {
        try {
            if (productId == null || productId <= 0) {
                return "error: Valid product ID is required";
            }
            
            StockTracking stock = stockRepository.findByProductId(productId);
            if (stock == null) {
                return "error: Product not found";
            }
            
            int newStock = stock.getCurrentStock() + quantity;
            if (newStock < 0) {
                return "error: Stock cannot be negative after update";
            }
            
            stock.setCurrentStock(newStock);
            stock.setLastUpdated(LocalDateTime.now());
            stockRepository.save(stock);
            return "success";
        } catch (Exception e) {
            System.err.println("Error updating stock: " + e.getMessage());
            return "error: Failed to update stock - " + e.getMessage();
        }
    }
    
    // Reduce stock (for sales)
    public String reduceStock(Long productId, int quantity) {
        try {
            if (productId == null || productId <= 0) {
                return "error: Valid product ID is required";
            }
            if (quantity <= 0) {
                return "error: Quantity must be positive";
            }
            
            StockTracking stock = stockRepository.findByProductId(productId);
            if (stock == null) {
                return "error: Product not found";
            }
            
            if (stock.getCurrentStock() < quantity) {
                return "error: Insufficient stock. Available: " + stock.getCurrentStock();
            }
            
            stock.setCurrentStock(stock.getCurrentStock() - quantity);
            stock.setLastUpdated(LocalDateTime.now());
            stockRepository.save(stock);
            return "success";
        } catch (Exception e) {
            System.err.println("Error reducing stock: " + e.getMessage());
            return "error: Failed to reduce stock - " + e.getMessage();
        }
    }
    
    // Get stock by product ID
    public StockTracking getStockByProductId(Long productId) {
        try {
            if (productId == null || productId <= 0) {
                return null;
            }
            return stockRepository.findByProductId(productId);
        } catch (Exception e) {
            System.err.println("Error getting stock by product ID: " + e.getMessage());
            return null;
        }
    }
    
    // Get all stock
    public List<StockTracking> getAllStock() {
        try {
            return stockRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all stock: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    // Get low stock items
    public List<StockTracking> getLowStockItems() {
        try {
            return stockRepository.findLowStockItems();
        } catch (Exception e) {
            System.err.println("Error getting low stock items: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    // Get items needing reorder
    public List<StockTracking> getItemsNeedingReorder() {
        try {
            return stockRepository.findItemsNeedingReorder();
        } catch (Exception e) {
            System.err.println("Error getting items needing reorder: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    // Search stock by product name
    public List<StockTracking> searchStockByProductName(String productName) {
        try {
            if (productName == null || productName.trim().isEmpty()) {
                return getAllStock(); // Return all if search term is empty
            }
            
            String searchTerm = productName.toLowerCase().trim();
            List<StockTracking> results = stockRepository.findByProductNameContaining(searchTerm);
            
            // Additional filtering for case-insensitive search
            List<StockTracking> filteredResults = new java.util.ArrayList<>();
            for (StockTracking stock : results) {
                if (stock.getProduct() != null && stock.getProduct().getName() != null && 
                    stock.getProduct().getName().toLowerCase().contains(searchTerm)) {
                    filteredResults.add(stock);
                }
            }
            return filteredResults;
        } catch (Exception e) {
            System.err.println("Error searching stock: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    // Delete stock entry
    public String deleteStock(Long stockId) {
        try {
            if (stockId == null || stockId <= 0) {
                return "error: Valid stock ID is required";
            }
            
            if (!stockRepository.existsById(stockId)) {
                return "error: Stock entry not found";
            }
            
            stockRepository.deleteById(stockId);
            return "success";
        } catch (Exception e) {
            System.err.println("Error deleting stock: " + e.getMessage());
            return "error: Failed to delete stock - " + e.getMessage();
        }
    }
    
    // Check if stock is low
    public boolean isStockLow(Long productId) {
        try {
            StockTracking stock = getStockByProductId(productId);
            return stock != null && stock.getCurrentStock() <= stock.getMinimumStock();
        } catch (Exception e) {
            System.err.println("Error checking low stock: " + e.getMessage());
            return false;
        }
    }
    
    // Get stock status summary
    public String getStockStatus(Long productId) {
        try {
            StockTracking stock = getStockByProductId(productId);
            if (stock == null) {
                return "Product not found";
            }
            
            if (stock.getCurrentStock() <= 0) {
                return "Out of stock";
            } else if (stock.getCurrentStock() <= stock.getMinimumStock()) {
                return "Low stock - Reorder needed";
            } else {
                return "In stock";
            }
        } catch (Exception e) {
            System.err.println("Error getting stock status: " + e.getMessage());
            return "Error checking stock status";
        }
    }
}