package com.retail_management.restcontroller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.retail_management.entity.Sales;
import com.retail_management.service.SalesService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SalesRestController {

    @Autowired
    private SalesService salesService;

    // Create a new sale
    @PostMapping("/save/sale")
    public ResponseEntity<String> createSale(@RequestParam Long productId, 
                                           @RequestParam Long customerId, 
                                           @RequestParam int quantity, 
                                           @RequestParam(required = false) String paymentMethod,
                                           @RequestParam(required = false) String notes) {
        String result = salesService.createSale(productId, customerId, quantity, paymentMethod, notes);
        if (result.startsWith("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Get all sales
    @GetMapping("/list/sales")
    public ResponseEntity<List<Sales>> getAllSales() {
        List<Sales> sales = salesService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    // Get sale by ID
    @GetMapping("/sale/{saleId}")
    public ResponseEntity<Sales> getSaleById(@PathVariable Long saleId) {
        Sales sale = salesService.getSaleById(saleId);
        if (sale != null) {
            return ResponseEntity.ok(sale);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get sales by customer
    @GetMapping("/sales/customer/{customerId}")
    public ResponseEntity<List<Sales>> getSalesByCustomer(@PathVariable Long customerId) {
        List<Sales> sales = salesService.getSalesByCustomer(customerId);
        return ResponseEntity.ok(sales);
    }

    // Get sales by product
    @GetMapping("/sales/product/{productId}")
    public ResponseEntity<List<Sales>> getSalesByProduct(@PathVariable Long productId) {
        List<Sales> sales = salesService.getSalesByProduct(productId);
        return ResponseEntity.ok(sales);
    }

    // Get sales by date range
    @GetMapping("/sales/date-range")
    public ResponseEntity<List<Sales>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Sales> sales = salesService.getSalesByDateRange(startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    // Get sales by specific date
    @GetMapping("/sales/date")
    public ResponseEntity<List<Sales>> getSalesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Sales> sales = salesService.getSalesByDate(date);
        return ResponseEntity.ok(sales);
    }

    // Get recent sales (last N days)
    @GetMapping("/sales/recent")
    public ResponseEntity<List<Sales>> getRecentSales(@RequestParam(defaultValue = "7") int days) {
        List<Sales> sales = salesService.getRecentSales(days);
        return ResponseEntity.ok(sales);
    }

    // Get sales statistics
    @GetMapping("/sales/statistics")
    public ResponseEntity<Map<String, Object>> getSalesStatistics() {
        Map<String, Object> stats = salesService.getSalesStatistics();
        return ResponseEntity.ok(stats);
    }

    // Get sales statistics by date range
    @GetMapping("/sales/statistics/date-range")
    public ResponseEntity<Map<String, Object>> getSalesStatisticsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> stats = salesService.getSalesStatisticsByDateRange(startDate, endDate);
        return ResponseEntity.ok(stats);
    }

    // Get total sales amount by date range
    @GetMapping("/sales/total-amount")
    public ResponseEntity<Double> getTotalSalesAmountByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Double total = salesService.getTotalSalesAmountByDateRange(startDate, endDate);
        return ResponseEntity.ok(total);
    }

    // Get total sales amount by customer
    @GetMapping("/sales/total-amount/customer/{customerId}")
    public ResponseEntity<Double> getTotalSalesAmountByCustomer(@PathVariable Long customerId) {
        Double total = salesService.getTotalSalesAmountByCustomer(customerId);
        return ResponseEntity.ok(total);
    }

    // Get total sales amount by product
    @GetMapping("/sales/total-amount/product/{productId}")
    public ResponseEntity<Double> getTotalSalesAmountByProduct(@PathVariable Long productId) {
        Double total = salesService.getTotalSalesAmountByProduct(productId);
        return ResponseEntity.ok(total);
    }

    // Get top selling products
    @GetMapping("/sales/top-products")
    public ResponseEntity<List<Object[]>> getTopSellingProducts() {
        List<Object[]> topProducts = salesService.getTopSellingProducts();
        return ResponseEntity.ok(topProducts);
    }

    // Get top customers
    @GetMapping("/sales/top-customers")
    public ResponseEntity<List<Object[]>> getTopCustomers() {
        List<Object[]> topCustomers = salesService.getTopCustomers();
        return ResponseEntity.ok(topCustomers);
    }

    // Get daily sales summary
    @GetMapping("/sales/daily-summary")
    public ResponseEntity<List<Object[]>> getDailySalesSummary() {
        List<Object[]> dailySummary = salesService.getDailySalesSummary();
        return ResponseEntity.ok(dailySummary);
    }

    // Get monthly sales summary
    @GetMapping("/sales/monthly-summary")
    public ResponseEntity<List<Object[]>> getMonthlySalesSummary() {
        List<Object[]> monthlySummary = salesService.getMonthlySalesSummary();
        return ResponseEntity.ok(monthlySummary);
    }

    // Get sales by payment method
    @GetMapping("/sales/payment-method/{paymentMethod}")
    public ResponseEntity<List<Sales>> getSalesByPaymentMethod(@PathVariable String paymentMethod) {
        List<Sales> sales = salesService.getSalesByPaymentMethod(paymentMethod);
        return ResponseEntity.ok(sales);
    }

    // Get total sales by payment method
    @GetMapping("/sales/total-by-payment-method")
    public ResponseEntity<List<Object[]>> getTotalSalesByPaymentMethod() {
        List<Object[]> totals = salesService.getTotalSalesByPaymentMethod();
        return ResponseEntity.ok(totals);
    }

    // Search sales by customer name
    @GetMapping("/sales/search/customer")
    public ResponseEntity<List<Sales>> searchSalesByCustomerName(@RequestParam String customerName) {
        List<Sales> sales = salesService.searchSalesByCustomerName(customerName);
        return ResponseEntity.ok(sales);
    }

    // Search sales by product name
    @GetMapping("/sales/search/product")
    public ResponseEntity<List<Sales>> searchSalesByProductName(@RequestParam String productName) {
        List<Sales> sales = salesService.searchSalesByProductName(productName);
        return ResponseEntity.ok(sales);
    }

    // Get sales with amount greater than
    @GetMapping("/sales/amount-greater-than")
    public ResponseEntity<List<Sales>> getSalesWithAmountGreaterThan(@RequestParam Double amount) {
        List<Sales> sales = salesService.getSalesWithAmountGreaterThan(amount);
        return ResponseEntity.ok(sales);
    }

    // Get sales with amount less than
    @GetMapping("/sales/amount-less-than")
    public ResponseEntity<List<Sales>> getSalesWithAmountLessThan(@RequestParam Double amount) {
        List<Sales> sales = salesService.getSalesWithAmountLessThan(amount);
        return ResponseEntity.ok(sales);
    }

    // Update sale status
    @PutMapping("/sale/{saleId}/status")
    public ResponseEntity<String> updateSaleStatus(@PathVariable Long saleId, @RequestParam String status) {
        String result = salesService.updateSaleStatus(saleId, status);
        if (result.startsWith("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Delete sale
    @DeleteMapping("/delete/sale/{saleId}")
    public ResponseEntity<String> deleteSale(@PathVariable Long saleId) {
        String result = salesService.deleteSale(saleId);
        if (result.startsWith("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Get sales count by date range
    @GetMapping("/sales/count")
    public ResponseEntity<Long> getSalesCountByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long count = salesService.getSalesCountByDateRange(startDate, endDate);
        return ResponseEntity.ok(count);
    }

    // Get average sale amount by date range
    @GetMapping("/sales/average-amount")
    public ResponseEntity<Double> getAverageSaleAmountByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Double average = salesService.getAverageSaleAmountByDateRange(startDate, endDate);
        return ResponseEntity.ok(average);
    }
}
