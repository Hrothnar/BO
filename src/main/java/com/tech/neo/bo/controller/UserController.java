package com.tech.neo.bo.controller;

import com.tech.neo.bo.domain.entity.User;
import com.tech.neo.bo.domain.entity.dto.EmailDto;
import com.tech.neo.bo.domain.entity.dto.PhoneDto;
import com.tech.neo.bo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping(path = "/user")
@Validated()
public class UserController {
    private final UserService userService;

    @Autowired()
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<String> authenticatedUser() {
        User user = userService.getCurrentUser();
        System.out.println(user.getUsername());
        return ResponseEntity.ok(user.getUsername());
    }

    @PatchMapping("/phone")
    public ResponseEntity<String> updatePhoneNumber(@RequestBody() PhoneDto phoneDto) {
        userService.updateUserPhoneNumber(userService.getCurrentUserId(), phoneDto.getOldPhoneNumber(), phoneDto.getNewPhoneNumber());
        return ResponseEntity.ok("Phone number has been updated");
    }

    @PostMapping("/phone")
    public ResponseEntity<String> addPhoneNumber(@RequestBody() PhoneDto phoneDto) {
        userService.addUserPhoneNumber(userService.getCurrentUserId(), phoneDto.getNewPhoneNumber());
        return ResponseEntity.ok("Phone number has been added");
    }

    @DeleteMapping("/phone")
    public ResponseEntity<String> deletePhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        userService.removePhoneNumber(userService.getCurrentUserId(), phoneNumber);
        return ResponseEntity.ok("Phone number has been deleted");
    }

    @PatchMapping("/email")
    public ResponseEntity<String> updateEmail(@RequestBody() EmailDto emailDto) {
        userService.updateUserEmail(userService.getCurrentUserId(), emailDto.getOldEmail(), emailDto.getNewEmail());
        return ResponseEntity.ok("Email has been updated");
    }

    @PostMapping("/email")
    public ResponseEntity<String> addEmail(@RequestBody() EmailDto emailDto) {
        userService.addEmail(userService.getCurrentUserId(), emailDto.getNewEmail());
        return ResponseEntity.ok("Email has been added");
    }

    @DeleteMapping("/email")
    public ResponseEntity<String> deleteEmail(@RequestParam("email") String email) {
        userService.removeEmail(userService.getCurrentUserId(), email);
        return ResponseEntity.ok("Email has been deleted");
    }

    // search endpoints below ===========================================

    @GetMapping("/birthDate")
    public ResponseEntity<List<User>> findAll(@RequestParam("v") LocalDate localDate) {
        List<User> users = userService.findPeopleYoungerThan(localDate);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/fullName")
    public ResponseEntity<List<User>> findAll(@RequestParam("v") String fullName) {
        List<User> users = userService.findAllByFullName(fullName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/phoneNumber")
    public ResponseEntity<User> findOneByPhoneNumber(@RequestParam("v") String phoneNumber) {
        User user = userService.findOneByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email")
    public ResponseEntity<User> findOneByEmail(@RequestParam("v") String email) {
        User user = userService.findOneByEmail(email);
        return ResponseEntity.ok(user);
    }

    // balance transfer below ==========================================

    @GetMapping("/account/transfer")
    public ResponseEntity<String> transfer(@RequestParam("to") long accountId, @RequestParam("amount") double amount) {
        userService.transferMoney(userService.getCurrentUserId(), accountId, amount);
        return ResponseEntity.ok("Money has been transferred successfully");
    }
}
