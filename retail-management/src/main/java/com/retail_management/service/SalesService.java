package com.retail_management.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail_management.dao.SalesDAO;
import com.retail_management.dao.StockRepository;
import com.retail_management.entity.Sales;
import com.retail_management.entity.Product;
import com.retail_management.entity.Customer;
import com.retail_management.entity.StockTracking;

@Service
public class SalesService {
    
    @Autowired
    private SalesDAO salesDAO;
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CustomerService customerService;
    
    // Create a new sale
    public String createSale(Long productId, Long customerId, int quantity, String paymentMethod, String notes) {
        try {
            // Validate inputs
            if (productId == null || customerId == null || quantity <= 0) {
                return "error: Invalid input parameters";
            }
            
            // Get product
            Product product = productService.getProductById(productId);
            if (product == null) {
                return "error: Product not found";
            }
            
            // Get customer
            Customer customer = customerService.getCustomerById(customerId);
            if (customer == null) {
                return "error: Customer not found";
            }
            
            // Check stock availability
            StockTracking stock = stockRepository.findByProductId(productId);
            if (stock == null) {
                return "error: Stock not found for this product";
            }
            
            if (stock.getCurrentStock() < quantity) {
                return "error: Insufficient stock. Available: " + stock.getCurrentStock();
            }
            
            // Create sale
            Sales sale = new Sales();
            sale.setProduct(product);
            sale.setCustomer(customer);
            sale.setQuantity(quantity);
            sale.setUnitPrice(product.getPrice());
            sale.calculateTotalAmount();
            sale.setPaymentMethod(paymentMethod != null ? paymentMethod : "CASH");
            sale.setNotes(notes);
            sale.setStatus("COMPLETED");
            
            // Save sale
            salesDAO.save(sale);
            
            // Reduce stock
            stock.setCurrentStock(stock.getCurrentStock() - quantity);
            stock.setLastUpdated(LocalDateTime.now());
            stockRepository.save(stock);
            
            return "success: Sale created successfully. Sale ID: " + sale.getId();
            
        } catch (Exception e) {
            System.err.println("Error creating sale: " + e.getMessage());
            return "error: Failed to create sale - " + e.getMessage();
        }
    }
    
    // Get all sales
    public List<Sales> getAllSales() {
        return salesDAO.findAll();
    }
    
    // Get sale by ID
    public Sales getSaleById(Long saleId) {
        Optional<Sales> sale = salesDAO.findById(saleId);
        return sale.orElse(null);
    }
    
    // Get sales by customer
    public List<Sales> getSalesByCustomer(Long customerId) {
        return salesDAO.findByCustomerId(customerId);
    }
    
    // Get sales by product
    public List<Sales> getSalesByProduct(Long productId) {
        return salesDAO.findByProductId(productId);
    }
    
    // Get sales by date range
    public List<Sales> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        return salesDAO.findByDateRange(startDate, endDate);
    }
    
    // Get sales by specific date
    public List<Sales> getSalesByDate(LocalDate date) {
        return salesDAO.findBySaleDate(date);
    }
    
    // Get recent sales (last N days)
    public List<Sales> getRecentSales(int days) {
        LocalDate fromDate = LocalDate.now().minusDays(days);
        return salesDAO.findRecentSales(fromDate);
    }
    
    // Get total sales amount by date range
    public Double getTotalSalesAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        Double total = salesDAO.getTotalSalesAmountByDateRange(startDate, endDate);
        return total != null ? total : 0.0;
    }
    
    // Get total sales amount by customer
    public Double getTotalSalesAmountByCustomer(Long customerId) {
        Double total = salesDAO.getTotalSalesAmountByCustomer(customerId);
        return total != null ? total : 0.0;
    }
    
    // Get total sales amount by product
    public Double getTotalSalesAmountByProduct(Long productId) {
        Double total = salesDAO.getTotalSalesAmountByProduct(productId);
        return total != null ? total : 0.0;
    }
    
    // Get sales statistics
    public Map<String, Object> getSalesStatistics() {
        Object[] stats = salesDAO.getSalesStatistics();
        Map<String, Object> result = new HashMap<>();
        
        if (stats != null && stats.length >= 5) {
            result.put("totalSales", stats[0]);
            result.put("totalAmount", stats[1]);
            result.put("averageAmount", stats[2]);
            result.put("minAmount", stats[3]);
            result.put("maxAmount", stats[4]);
        }
        
        return result;
    }
    
    // Get sales statistics by date range
    public Map<String, Object> getSalesStatisticsByDateRange(LocalDate startDate, LocalDate endDate) {
        Object[] stats = salesDAO.getSalesStatisticsByDateRange(startDate, endDate);
        Map<String, Object> result = new HashMap<>();
        
        if (stats != null && stats.length >= 5) {
            result.put("totalSales", stats[0]);
            result.put("totalAmount", stats[1]);
            result.put("averageAmount", stats[2]);
            result.put("minAmount", stats[3]);
            result.put("maxAmount", stats[4]);
        }
        
        return result;
    }
    
    // Get top selling products
    public List<Object[]> getTopSellingProducts() {
        return salesDAO.getTopSellingProducts();
    }
    
    // Get top customers
    public List<Object[]> getTopCustomers() {
        return salesDAO.getTopCustomers();
    }
    
    // Get daily sales summary
    public List<Object[]> getDailySalesSummary() {
        return salesDAO.getDailySalesSummary();
    }
    
    // Get monthly sales summary
    public List<Object[]> getMonthlySalesSummary() {
        return salesDAO.getMonthlySalesSummary();
    }
    
    // Get sales by payment method
    public List<Sales> getSalesByPaymentMethod(String paymentMethod) {
        return salesDAO.findByPaymentMethod(paymentMethod);
    }
    
    // Get total sales by payment method
    public List<Object[]> getTotalSalesByPaymentMethod() {
        return salesDAO.getTotalSalesByPaymentMethod();
    }
    
    // Search sales by customer name
    public List<Sales> searchSalesByCustomerName(String customerName) {
        return salesDAO.findByCustomerNameContaining(customerName);
    }
    
    // Search sales by product name
    public List<Sales> searchSalesByProductName(String productName) {
        return salesDAO.findByProductNameContaining(productName);
    }
    
    // Get sales with amount greater than
    public List<Sales> getSalesWithAmountGreaterThan(Double amount) {
        return salesDAO.findByAmountGreaterThan(amount);
    }
    
    // Get sales with amount less than
    public List<Sales> getSalesWithAmountLessThan(Double amount) {
        return salesDAO.findByAmountLessThan(amount);
    }
    
    // Update sale status
    public String updateSaleStatus(Long saleId, String status) {
        try {
            Sales sale = getSaleById(saleId);
            if (sale == null) {
                return "error: Sale not found";
            }
            
            sale.setStatus(status);
            salesDAO.save(sale);
            return "success: Sale status updated successfully";
            
        } catch (Exception e) {
            System.err.println("Error updating sale status: " + e.getMessage());
            return "error: Failed to update sale status - " + e.getMessage();
        }
    }
    
    // Delete sale (with stock restoration)
    public String deleteSale(Long saleId) {
        try {
            Sales sale = getSaleById(saleId);
            if (sale == null) {
                return "error: Sale not found";
            }
            
            // Restore stock
            StockTracking stock = stockRepository.findByProductId(sale.getProductId());
            if (stock != null) {
                stock.setCurrentStock(stock.getCurrentStock() + sale.getQuantity());
                stock.setLastUpdated(LocalDateTime.now());
                stockRepository.save(stock);
            }
            
            // Delete sale
            salesDAO.deleteById(saleId);
            return "success: Sale deleted successfully";
            
        } catch (Exception e) {
            System.err.println("Error deleting sale: " + e.getMessage());
            return "error: Failed to delete sale - " + e.getMessage();
        }
    }
    
    // Get sales count by date range
    public Long getSalesCountByDateRange(LocalDate startDate, LocalDate endDate) {
        return salesDAO.getSalesCountByDateRange(startDate, endDate);
    }
    
    // Get average sale amount by date range
    public Double getAverageSaleAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        Double average = salesDAO.getAverageSaleAmountByDateRange(startDate, endDate);
        return average != null ? average : 0.0;
    }
}
