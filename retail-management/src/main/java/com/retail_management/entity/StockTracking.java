package com.retail_management.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_tracking")
public class StockTracking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    
    @Column(name = "current_stock")
    private int currentStock;
    
    @Column(name = "minimum_stock")
    private int minimumStock;
    
    @Column(name = "reorder_point")
    private int reorderPoint;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    // Constructors
    public StockTracking() {}
    
    public StockTracking(Product product, int currentStock, int minimumStock, int reorderPoint) {
        this.product = product;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.reorderPoint = reorderPoint;
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    // Helper methods to get product information
    public Integer getProductId() {
        return product != null ? product.getId() : null;
    }
    
    public String getProductName() {
        return product != null ? product.getName() : null;
    }
    
    public int getCurrentStock() {
        return currentStock;
    }
    
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }
    
    public int getMinimumStock() {
        return minimumStock;
    }
    
    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }
    
    public int getReorderPoint() {
        return reorderPoint;
    }
    
    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    // Alias method for compatibility
    public int getMinStockLevel() {
        return minimumStock;
    }
}