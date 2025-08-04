package com.retail_management.restcontroller;

public class SalesRestController {
//	package com.retail_management.restcontroller;
//
//	import com.retail_management.entity.Sales;
//	import com.retail_management.service.SalesService;
//	import org.springframework.beans.factory.annotation.Autowired;
//	import org.springframework.web.bind.annotation.*;
//
//	import java.util.List;
//
//	@RestController
//	@RequestMapping("/api/sales")
//	public class SalesRestController {
//
//	    @Autowired
//	    private SalesService salesService;
//
//	    // Get all sales
//	    @GetMapping
//	    public List<Sales> getAllSales() {
//	        return salesService.getAllSales();
//	    }
//
//	    // Get sale by ID
//	    @GetMapping("/{id}")
//	    public Sales getSaleById(@PathVariable int id) {
//	        return salesService.getSaleById(id);
//	    }
//
//	    // Add a new sale
//	    @PostMapping
//	    public Sales addSale(@RequestBody Sales sale) {
//	        return salesService.saveSale(sale);
//	    }
//
//	    // Update a sale
//	    @PutMapping("/{id}")
//	    public Sales updateSale(@PathVariable int id, @RequestBody Sales updatedSale) {
//	        updatedSale.setId(id);
//	        return salesService.saveSale(updatedSale);
//	    }
//
//	    // Delete a sale
//	    @DeleteMapping("/{id}")
//	    public String deleteSale(@PathVariable int id) {
//	        salesService.deleteSale(id);
//	        return "Deleted sale with id: " + id;
//	    }
//	}


}
