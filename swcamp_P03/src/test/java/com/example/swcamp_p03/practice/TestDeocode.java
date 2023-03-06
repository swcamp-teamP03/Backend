package com.example.swcamp_p03.practice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class TestDeocode {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void test() {
        String a = "aaa";
        String aaa = bCryptPasswordEncoder.encode(a);
        boolean matches = bCryptPasswordEncoder.matches("aaa", aaa);
        System.out.println(matches);
    }
}
