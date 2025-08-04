package com.retail_management.restcontroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.retail_management.entity.StockTracking;
import com.retail_management.service.StockService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StockRestController {
    
    @Autowired
    private StockService stockService;
    
    // Add new stock
    @PostMapping("/save/stock")
    public ResponseEntity<String> addStock(@RequestBody StockTracking stock) {
        String result = stockService.addStock(stock);
        if (result.startsWith("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    // Get all stock
    @GetMapping("/list/stock")
    public ResponseEntity<List<StockTracking>> getAllStock() {
        List<StockTracking> stockList = stockService.getAllStock();
        return ResponseEntity.ok(stockList);
    }
    
    // Update stock
    @PostMapping("/update/stock")
    public ResponseEntity<String> updateStock(@RequestParam Long productId, @RequestParam int quantity) {
        String result = stockService.updateStock(productId, quantity);
        if (result.startsWith("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    // Reduce stock
    @PostMapping("/reduce/stock")
    public ResponseEntity<String> reduceStock(@RequestParam Long productId, @RequestParam int quantity) {
        String result = stockService.reduceStock(productId, quantity);
        if (result.startsWith("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    // Get stock by product ID
    @GetMapping("/stock/{productId}")
    public ResponseEntity<StockTracking> getStockByProductId(@PathVariable Long productId) {
        StockTracking stock = stockService.getStockByProductId(productId);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Search stock by product name
    @GetMapping("/search/stock")
    public ResponseEntity<List<StockTracking>> searchStock(@RequestParam String productName) {
        List<StockTracking> results = stockService.searchStockByProductName(productName);
        return ResponseEntity.ok(results);
    }
    
    // Get low stock items
    @GetMapping("/low-stock")
    public ResponseEntity<List<StockTracking>> getLowStockItems() {
        List<StockTracking> lowStockItems = stockService.getLowStockItems();
        return ResponseEntity.ok(lowStockItems);
    }
    
    // Delete stock
    @DeleteMapping("/delete/stock/{stockId}")
    public ResponseEntity<String> deleteStock(@PathVariable Long stockId) {
        String result = stockService.deleteStock(stockId);
        if (result.startsWith("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    // Get stock status
    @GetMapping("/stock-status/{productId}")
    public ResponseEntity<String> getStockStatus(@PathVariable Long productId) {
        String status = stockService.getStockStatus(productId);
        return ResponseEntity.ok(status);
    }
}