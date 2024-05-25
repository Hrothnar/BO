package com.tech.neo.bo.controller;

import com.tech.neo.bo.domain.entity.User;
import com.tech.neo.bo.domain.entity.dto.EmailDto;
import com.tech.neo.bo.domain.entity.dto.PhoneDto;
import com.tech.neo.bo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "User", description = "Endpoints for interacting with users")
@RestController()
@RequestMapping(path = "/user")
@Validated()
public class UserController {
    private final UserService userService;

    @Autowired()
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Update my phone number",
            responses = @ApiResponse(responseCode = "200", description = "Phone number has been updated",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            examples = @ExampleObject("Phone number has been updated"))))
    @PatchMapping("/phone")
    public ResponseEntity<String> updatePhoneNumber(@RequestBody() PhoneDto phoneDto) {
        userService.updateUserPhoneNumber(userService.getCurrentUserId(), phoneDto.getOldPhoneNumber(), phoneDto.getNewPhoneNumber());
        return ResponseEntity.ok("Phone number has been updated");
    }

    @Operation(summary = "Add phone number",
            responses = @ApiResponse(responseCode = "200", description = "Phone number has been added",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            examples = @ExampleObject("Phone number has been added"))))
    @PostMapping("/phone")
    public ResponseEntity<String> addPhoneNumber(@RequestBody() PhoneDto phoneDto) {
        userService.addUserPhoneNumber(userService.getCurrentUserId(), phoneDto.getNewPhoneNumber());
        return ResponseEntity.ok("Phone number has been added");
    }

    @Operation(summary = "Delete my phone number",
            responses = @ApiResponse(responseCode = "200", description = "Phone number has been deleted",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            examples = @ExampleObject("Phone number has been deleted"))))
    @DeleteMapping("/phone")
    public ResponseEntity<String> deletePhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        userService.removePhoneNumber(userService.getCurrentUserId(), phoneNumber);
        return ResponseEntity.ok("Phone number has been deleted");
    }

    @Operation(summary = "Update my email",
            responses = @ApiResponse(responseCode = "200", description = "Email has been updated",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            examples = @ExampleObject("Email has been updated"))))
    @PatchMapping("/email")
    public ResponseEntity<String> updateEmail(@RequestBody() EmailDto emailDto) {
        userService.updateUserEmail(userService.getCurrentUserId(), emailDto.getOldEmail(), emailDto.getNewEmail());
        return ResponseEntity.ok("Email has been updated");
    }

    @Operation(summary = "Add email",
            responses = @ApiResponse(responseCode = "200", description = "Email has been added",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            examples = @ExampleObject("Email has been added"))))
    @PostMapping("/email")
    public ResponseEntity<String> addEmail(@RequestBody() EmailDto emailDto) {
        userService.addEmail(userService.getCurrentUserId(), emailDto.getNewEmail());
        return ResponseEntity.ok("Email has been added");
    }

    @Operation(summary = "Delete my email",
            responses = @ApiResponse(responseCode = "200", description = "Email has been deleted",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            examples = @ExampleObject("Email has been deleted"))))
    @DeleteMapping("/email")
    public ResponseEntity<String> deleteEmail(@RequestParam("email") String email) {
        userService.removeEmail(userService.getCurrentUserId(), email);
        return ResponseEntity.ok("Email has been deleted");
    }

    // search endpoints below ===========================================

    @Operation(summary = "Find all people who are younger than a given date",
            responses = @ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping("/birthDate")
    public ResponseEntity<List<User>> findAllYoungerThan(@RequestParam("v") LocalDate localDate) {
        List<User> users = userService.findPeopleYoungerThan(localDate);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Find all people whose name is similar to the one given",
            responses = @ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping("/fullName")
    public ResponseEntity<List<User>> findAllWhereNameLike(@RequestParam("v") String fullName) {
        List<User> users = userService.findAllByFullName(fullName);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Find a user who has a given phone number",
            responses = @ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping("/phoneNumber")
    public ResponseEntity<User> findOneByPhoneNumber(@RequestParam("v") String phoneNumber) {
        User user = userService.findOneByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Find a user who has a given email",
            responses = @ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping("/email")
    public ResponseEntity<User> findOneByEmail(@RequestParam("v") String email) {
        User user = userService.findOneByEmail(email);
        return ResponseEntity.ok(user);
    }

    // balance transfer below ==========================================

    @Operation(summary = "Transfer a certain amount of money from my account to a specified one",
            responses = @ApiResponse(responseCode = "200", description = "Money has been transferred successfully",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            examples = @ExampleObject("Money has been transferred successfully"))))
    @GetMapping("/account/transfer")
    public ResponseEntity<String> transfer(@RequestParam("to") long accountId, @RequestParam("amount") double amount) {
        userService.transferMoney(userService.getCurrentUserId(), accountId, amount);
        return ResponseEntity.ok("Money has been transferred successfully");
    }
}
