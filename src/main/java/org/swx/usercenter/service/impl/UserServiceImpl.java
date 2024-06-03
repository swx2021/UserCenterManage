package org.swx.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.swx.usercenter.common.ErrorCode;
import org.swx.usercenter.exception.BusinessException;
import org.swx.usercenter.model.User;
import org.swx.usercenter.service.UserService;
import org.swx.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

import static org.swx.usercenter.Constant.UserConstant.USER_LOGIN_STATE;

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


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验账户密码确认密码是否为空
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //2.校验账户长度是否合法
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户长度过短");
        }

        //3.校验账户密码以及确认密码是否合法
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }
        //4.校验账户是否包含特殊字符
        String regex = "^[a-zA-Z0-9_-]+$";
        if (!Pattern.matches(regex, userAccount)) {
            throw new BusinessException(ErrorCode.NO_REGISTER,"无法注册：账号包含特殊字符");
        }
        //5.校验密码和确认密码是否相同
        if(!checkPassword.equals(userPassword)){
            throw new BusinessException(ErrorCode.NO_REGISTER,"无法注册：密码和确认密码不同");
        }
        //6.校验账户和会员ID是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        //long count = this.count(queryWrapper);
        Long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.NO_REGISTER,"无法注册：账号重复");
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
            throw new BusinessException(ErrorCode.NO_REGISTER,"无法注册：用户插入数据库失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验账户密码是否为空
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            log.info("userAccount or userPassword is empty");
            throw new BusinessException(ErrorCode.NO_LOGIN,"无法登录：账号或密码为空");
        }
        //2.校验账户长度是否合法
        if(userAccount.length() < 4){
            log.info("userAccount is less than 4");
            throw new BusinessException(ErrorCode.NO_LOGIN,"无法登录：账号长度不合法");
        }
        //3.校验账户密码是否合法
        if(userPassword.length() < 8){
            log.info("userPassword is less than 8");
            throw new BusinessException(ErrorCode.NO_LOGIN,"无法登录：密码不合法");
        }
        //4.校验账户是否包含特殊字符
        String regex = "^[a-zA-Z0-9_-]+$";
        if (!Pattern.matches(regex, userAccount)) {
            log.info("userAccount is invalid");
            throw new BusinessException(ErrorCode.NO_LOGIN,"无法登录：账号包含特殊字符");
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
            throw new BusinessException(ErrorCode.NO_LOGIN,"无法登录：用户不存在");
        }
        //7.用户脱敏,如果不脱敏，前端会看到所有从数据库返回的信息
        User safeUser = getSafeUser(user);
        //8.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safeUser);

        return safeUser;
    }

    @Override
    public User getSafeUser(User user){
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setMemberID(user.getMemberID());
        safeUser.setRole(user.getRole());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setCreateTime(user.getCreateTime());

        return safeUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User)userObj;
        if(currentUser == null){
            throw new BusinessException(ErrorCode.NO_LOGIN,"当前未登录，无法获取登录态");
        }
        long userId = currentUser.getId();
        //重新从数据库进行查询，确保数据是最新的
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        User user = userMapper.selectOne(queryWrapper);
        return getSafeUser(user);
    }
}




