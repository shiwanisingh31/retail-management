package com.retail_management.restcontroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail_management.entity.User;
import com.retail_management.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserRestController {

    @Autowired
    UserService userServ;

    @PostMapping("/save/user")
    public String saveUsers(@RequestBody User user) {
        System.out.println(user.getName() + " " + user.getPhoneno() + " " + user.getPassword());
        return userServ.saveUserToDB(user.getName(), user.getPhoneno(), user.getPassword());
    }

    @GetMapping("/list/user")
    public List<User> listUser() {
        return userServ.listUser();
    }

    @GetMapping("/delete/user")
    public String delUser(@RequestParam("id") int id) {
        return userServ.delUser(id);
    }

    @PostMapping({"/login", "/auth/login"})
    public ResponseEntity<?> login(@RequestBody User user, HttpSession session) {
        if (userServ.authenticate(user.getPhoneno(), user.getPassword())) {
            session.setAttribute("user", user.getPhoneno());
            return ResponseEntity.ok("Login success");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping({"/logout", "/auth/logout"})
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }

    @GetMapping({"/check-session", "/auth/check-session"})
    public ResponseEntity<String> checkSession(HttpSession session) {
        Object userObj = session.getAttribute("user");

        if (userObj != null) {
            return ResponseEntity.ok("Logged in as " + userObj.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
    }
}