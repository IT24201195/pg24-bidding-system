package com.pg24.bidding.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CheckoutRequest {
    @NotBlank @Size(max = 255)
    private String address;
    @NotBlank @Size(max = 32)
    private String phone;
    @Size(max = 1000)
    private String notes;

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
