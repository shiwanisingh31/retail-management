package com.retail_management.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
public class Sales {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    
    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "unit_price")
    private double unitPrice;
    
    @Column(name = "total_amount")
    private double totalAmount;
    
    @Column(name = "sale_date")
    private LocalDate saleDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "payment_method")
    private String paymentMethod;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "notes")
    private String notes;
    
    // Constructors
    public Sales() {
        this.saleDate = LocalDate.now();
        this.createdAt = LocalDateTime.now();
        this.status = "COMPLETED";
    }
    
    public Sales(Product product, Customer customer, int quantity, double unitPrice) {
        this();
        this.product = product;
        this.customer = customer;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = quantity * unitPrice;
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
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        // Recalculate total amount when quantity changes
        if (this.unitPrice > 0) {
            this.totalAmount = quantity * this.unitPrice;
        }
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        // Recalculate total amount when unit price changes
        if (this.quantity > 0) {
            this.totalAmount = this.quantity * unitPrice;
        }
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public LocalDate getSaleDate() {
        return saleDate;
    }
    
    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // Helper methods to get related entity information
    public Long getProductId() {
        return product != null ? product.getId() : null;
    }
    
    public String getProductName() {
        return product != null ? product.getName() : null;
    }
    
    public Long getCustomerId() {
        return customer != null ? customer.getId() : null;
    }
    
    public String getCustomerName() {
        return customer != null ? customer.getName() : null;
    }
    
    // Method to calculate total amount
    public void calculateTotalAmount() {
        if (this.quantity > 0 && this.unitPrice > 0) {
            this.totalAmount = this.quantity * this.unitPrice;
        }
    }
    
    @Override
    public String toString() {
        return "Sales{" +
                "id=" + id +
                ", product=" + (product != null ? product.getName() : "null") +
                ", customer=" + (customer != null ? customer.getName() : "null") +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalAmount=" + totalAmount +
                ", saleDate=" + saleDate +
                ", status='" + status + '\'' +
                '}';
    }
}
