package com.tech.neo.bo.domain.entity.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class UserDto {
    @NotBlank()
    @Size(min = 5, max = 64, message = "Full name should be between 5 and 64 chars")
    private String fullName;
    @NotBlank()
    @Size(min = 4, max = 64, message = "Login should be between 4 and 64 chars")
    private String login;
    @NotBlank()
    @Size(min = 4, max = 64, message = "Password should be between 4 and 64 chars")
    private String password;
    @NotBlank()
    @Size(min = 6, max = 16,  message = "Phone number should be between 5 and 64 chars")
    private String phoneNumber;
    @Email(message = "Email should be appropriate format")
    private String email;
    @NotNull()
    @Positive(message = "Deposit should be positive")
    private BigDecimal initialDeposit;
    private LocalDate birthDate;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLogin() {
        return login;
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        if (Objects.nonNull(birthDate) && birthDate.isBefore(LocalDate.now().minusYears(100))) {
            throw new RuntimeException("This guy is too old");
        }
        this.birthDate = birthDate;
    }
}
