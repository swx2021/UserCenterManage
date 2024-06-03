package org.swx.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Pattern;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testSpecial() {
        String regex = "^[a-zA-Z0-9_-]+$";
        if (!Pattern.matches(regex, "1234-766")) {
            System.out.println("有特殊字符");
        }
        else{
            System.out.println("无特殊字符");
        }
    }
}
