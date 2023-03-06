package com.example.swcamp_p03.common.valid;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidCheck {
    public Boolean isUsername(String username) {
        return Pattern.matches("^[가-힣|a-z|A-Z|]+$", username);
    }

    public Boolean isPhoneNumber(String phoneNumber) {
        return Pattern.matches("^(010|011|016|017|018|019)-[0-9]{3,4}-[0-9]{4}$", phoneNumber);
    }
}
