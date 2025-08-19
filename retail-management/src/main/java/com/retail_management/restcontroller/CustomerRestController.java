package com.retail_management.restcontroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.retail_management.entity.Customer;
import com.retail_management.service.CustomerService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CustomerRestController {
    
    @Autowired
    CustomerService customerServ;

    @PostMapping("/save/customer")
    public String saveCustomers(
            @RequestParam("name") String name,
            @RequestParam("phoneno") int phoneno,
            @RequestParam("email") String email,
            @RequestParam("gender") String gender,
            @RequestParam("age") int age){

        System.out.println(name + " " + phoneno+ " " + email+ " " + gender+ " " +age);

        return customerServ.saveCustomersToDB(name, phoneno, email, age, gender);
    }
    
    @GetMapping("/list/customer")
    public List<Customer> listCustomer() {
        return customerServ.listCustomers();
    }
    
    @GetMapping("/delete/customer")
    public String delCustomer(@RequestParam("id") Long id) {
        return customerServ.delCustomer(id);
    }
}