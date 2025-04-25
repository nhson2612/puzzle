package com.example.jigsawpuzzle.domain;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class Email {
    private final String address;

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public Email(String address) {
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + address);
        }
        this.address = address;
    }
    protected Email() {
        this.address = null; // for JPA
    }
    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return address;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Email)) return false;
        Email other = (Email) obj;
        return this.address.equalsIgnoreCase(other.address);
    }

    @Override
    public int hashCode() {
        return address.toLowerCase().hashCode();
    }
}
