package com.retail_management.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.retail_management.entity.Sales;

@Repository
public interface SalesDAO extends JpaRepository<Sales, Long> {
    
    // Find sales by customer ID
    @Query("SELECT s FROM Sales s WHERE s.customer.id = ?1")
    List<Sales> findByCustomerId(Long customerId);
    
    // Find sales by product ID
    @Query("SELECT s FROM Sales s WHERE s.product.id = ?1")
    List<Sales> findByProductId(Long productId);
    
    // Find sales by date range
    @Query("SELECT s FROM Sales s WHERE s.saleDate BETWEEN ?1 AND ?2")
    List<Sales> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Find sales by specific date
    @Query("SELECT s FROM Sales s WHERE DATE(s.saleDate) = ?1")
    List<Sales> findBySaleDate(LocalDate saleDate);
    
    // Find sales by customer name containing
    @Query("SELECT s FROM Sales s WHERE s.customer.name LIKE %?1%")
    List<Sales> findByCustomerNameContaining(String customerName);
    
    // Find sales by product name containing
    @Query("SELECT s FROM Sales s WHERE s.product.name LIKE %?1%")
    List<Sales> findByProductNameContaining(String productName);
    
    // Get total sales amount by date range
    @Query("SELECT SUM(s.totalAmount) FROM Sales s WHERE s.saleDate BETWEEN ?1 AND ?2")
    Double getTotalSalesAmountByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Get total sales amount by customer
    @Query("SELECT SUM(s.totalAmount) FROM Sales s WHERE s.customer.id = ?1")
    Double getTotalSalesAmountByCustomer(Long customerId);
    
    // Get total sales amount by product
    @Query("SELECT SUM(s.totalAmount) FROM Sales s WHERE s.product.id = ?1")
    Double getTotalSalesAmountByProduct(Long productId);
    
    // Get top selling products (by quantity)
    @Query("SELECT s.product.id, s.product.name, SUM(s.quantity) as totalQuantity FROM Sales s GROUP BY s.product.id, s.product.name ORDER BY totalQuantity DESC")
    List<Object[]> getTopSellingProducts();
    
    // Get top customers (by total amount)
    @Query("SELECT s.customer.id, s.customer.name, SUM(s.totalAmount) as totalAmount FROM Sales s GROUP BY s.customer.id, s.customer.name ORDER BY totalAmount DESC")
    List<Object[]> getTopCustomers();
    
    // Get daily sales summary
    @Query("SELECT DATE(s.saleDate) as saleDate, COUNT(s) as totalSales, SUM(s.totalAmount) as totalAmount FROM Sales s GROUP BY DATE(s.saleDate) ORDER BY saleDate DESC")
    List<Object[]> getDailySalesSummary();
    
    // Get monthly sales summary
    @Query("SELECT YEAR(s.saleDate) as year, MONTH(s.saleDate) as month, COUNT(s) as totalSales, SUM(s.totalAmount) as totalAmount FROM Sales s GROUP BY YEAR(s.saleDate), MONTH(s.saleDate) ORDER BY year DESC, month DESC")
    List<Object[]> getMonthlySalesSummary();
    
    // Find recent sales (last N days)
    @Query("SELECT s FROM Sales s WHERE s.saleDate >= ?1 ORDER BY s.saleDate DESC, s.id DESC")
    List<Sales> findRecentSales(LocalDate fromDate);
    
    // Get sales count by date range
    @Query("SELECT COUNT(s) FROM Sales s WHERE s.saleDate BETWEEN ?1 AND ?2")
    Long getSalesCountByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Get average sale amount by date range
    @Query("SELECT AVG(s.totalAmount) FROM Sales s WHERE s.saleDate BETWEEN ?1 AND ?2")
    Double getAverageSaleAmountByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Find sales with amount greater than
    @Query("SELECT s FROM Sales s WHERE s.totalAmount > ?1")
    List<Sales> findByAmountGreaterThan(Double amount);
    
    // Find sales with amount less than
    @Query("SELECT s FROM Sales s WHERE s.totalAmount < ?1")
    List<Sales> findByAmountLessThan(Double amount);
    
    // Get sales by payment method
    @Query("SELECT s FROM Sales s WHERE s.paymentMethod = ?1")
    List<Sales> findByPaymentMethod(String paymentMethod);
    
    // Get total sales by payment method
    @Query("SELECT s.paymentMethod, SUM(s.totalAmount) FROM Sales s GROUP BY s.paymentMethod")
    List<Object[]> getTotalSalesByPaymentMethod();
    
    // Find sales by status
    @Query("SELECT s FROM Sales s WHERE s.status = ?1")
    List<Sales> findByStatus(String status);
    
    // Get sales statistics
    @Query("SELECT COUNT(s), SUM(s.totalAmount), AVG(s.totalAmount), MIN(s.totalAmount), MAX(s.totalAmount) FROM Sales s")
    Object[] getSalesStatistics();
    
    // Get sales statistics by date range
    @Query("SELECT COUNT(s), SUM(s.totalAmount), AVG(s.totalAmount), MIN(s.totalAmount), MAX(s.totalAmount) FROM Sales s WHERE s.saleDate BETWEEN ?1 AND ?2")
    Object[] getSalesStatisticsByDateRange(LocalDate startDate, LocalDate endDate);
}
