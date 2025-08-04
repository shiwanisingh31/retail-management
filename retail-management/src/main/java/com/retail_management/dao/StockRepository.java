package com.retail_management.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.retail_management.entity.StockTracking;

@Repository
public interface StockRepository extends JpaRepository<StockTracking, Long> {
    
    // Find stock by product ID
    @Query("SELECT s FROM StockTracking s WHERE s.product.id = ?1")
    StockTracking findByProductId(Long productId);
    
    // Find stock by product name containing
    @Query("SELECT s FROM StockTracking s WHERE s.product.name LIKE %?1%")
    List<StockTracking> findByProductNameContaining(String productName);
    
    // Find low stock items (current stock <= minimum stock)
    @Query("SELECT s FROM StockTracking s WHERE s.currentStock <= s.minimumStock")
    List<StockTracking> findLowStockItems();
    
    // Find items needing reorder (current stock <= reorder point)
    @Query("SELECT s FROM StockTracking s WHERE s.currentStock <= s.reorderPoint")
    List<StockTracking> findItemsNeedingReorder();
    
    // Check if stock exists by product ID
    @Query("SELECT COUNT(s) > 0 FROM StockTracking s WHERE s.product.id = ?1")
    boolean existsByProductId(Long productId);
}