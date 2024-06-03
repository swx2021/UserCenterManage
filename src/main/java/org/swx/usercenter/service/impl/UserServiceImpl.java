package org.swx.usercenter.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.swx.usercenter.model.User;
import org.swx.usercenter.service.UserService;
import org.swx.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
* @author swx
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-06-02 15:34:56
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;
    /**
     * 盐值 混淆密码
     */
    private static final String SALT = "swx";
    /**
     * 用户登录态键
     */
    private static final String USER_LOGIN_STATE = "user_login_state";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验账户密码确认密码是否为空
        //  todo 修改为自定义异常
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        //2.校验账户长度是否合法
        if(userAccount.length() < 4){
            return -1;
        }
        //3.校验账户密码以及确认密码是否合法
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;
        }
        //4.校验账户是否包含特殊字符
        String regex = "^[a-zA-Z0-9_-]+$";
        if (!Pattern.matches(regex, userAccount)) {
            return -1;
        }
        //5.校验密码和确认密码是否相同
        if(!checkPassword.equals(userPassword)){
            return -1;
        }
        //6.校验账户是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        //long count = this.count(queryWrapper);
        Long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            return -1;
        }

        //7.对密码进行加密
        String encryptPassWord = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //8.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassWord);
        int insertRes = userMapper.insert(user);
        //boolean saveResult = this.save(user);
        //判断插入是否成功用于避免拆箱失败
        if(insertRes==0){
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验账户密码是否为空
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            log.info("userAccount or userPassword is empty");
            return null;
        }
        //2.校验账户长度是否合法
        if(userAccount.length() < 4){
            log.info("userAccount is less than 4");
            return null;
        }
        //3.校验账户密码是否合法
        if(userPassword.length() < 8){
            log.info("userPassword is less than 8");
            return null;
        }
        //4.校验账户是否包含特殊字符
        String regex = "^[a-zA-Z0-9_-]+$";
        if (!Pattern.matches(regex, userAccount)) {
            log.info("userAccount is invalid");
            return null;
        }
        //5.对密码进行加密
        String encryptPassWord = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //6.查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassWord);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("user not exist");
            return null;
        }
        //7.用户脱敏,如果不脱敏，前端会看到所有从数据库返回的信息
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setCreateTime(user.getCreateTime());
        //8.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safeUser);

        return safeUser;
    }
}




