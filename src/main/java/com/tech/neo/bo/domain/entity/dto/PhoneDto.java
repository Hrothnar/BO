package com.tech.neo.bo.domain.entity.dto;

public class PhoneDto {
    private String oldPhoneNumber;
    private String newPhoneNumber;

    public PhoneDto(String oldPhoneNumber, String newPhoneNumber) {
        this.oldPhoneNumber = oldPhoneNumber;
        this.newPhoneNumber = newPhoneNumber;
    }

    public PhoneDto(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }

    public String getOldPhoneNumber() {
        return oldPhoneNumber;
    }

    public void setOldPhoneNumber(String oldPhoneNumber) {
        this.oldPhoneNumber = oldPhoneNumber;
    }

    public String getNewPhoneNumber() {
        return newPhoneNumber;
    }

    public void setNewPhoneNumber(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }
}
