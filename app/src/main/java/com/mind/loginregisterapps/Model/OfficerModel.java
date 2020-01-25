package com.mind.loginregisterapps.Model;

public class OfficerModel {
    private String username, email, phone, profile_pix;

    public OfficerModel() {
    }

    public OfficerModel(String username, String email, String phone, String profile_pix) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.profile_pix = profile_pix;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_pix() {
        return profile_pix;
    }

    public void setProfile_pix(String profile_pix) {
        this.profile_pix = profile_pix;
    }
}
