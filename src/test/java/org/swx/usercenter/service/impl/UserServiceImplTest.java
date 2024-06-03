package org.swx.usercenter.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void resgisterTest(){
        //1.校验账户密码以及确认密码是否为空
        String userAccount = "";
        String userPassword = "123456789555";
        String checkPassword = "123456789555";
        String memberID = "1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //2.校验账户长度和会员长度是否合法
        userAccount = "12";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);

        memberID = "2215151";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //3.校验账户密码以及确认密码是否合法
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //4.校验账户是否包含特殊字符
        userAccount = "123/454784";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //5.校验密码和确认密码是否相同
        userPassword = "124451215151";
        checkPassword = "1548115154151";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //6.校验账户和会员ID是否重复
        userAccount = "123";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);

        memberID = "123";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //7.判断加密是否成功
        userPassword = "123456521511515";
        String encryptPassWord = DigestUtils.md5DigestAsHex(("abcd" + userPassword).getBytes());
        System.out.println(encryptPassWord);
        //8.在所有信息都正确的情况下判断插入是否成功
        userAccount = "zhangjiaxin2";
        userPassword = "12141312151";
        checkPassword = "12141312151";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);
    }
}