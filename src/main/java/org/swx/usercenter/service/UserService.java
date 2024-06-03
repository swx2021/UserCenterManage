package org.swx.usercenter.service;

import org.swx.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 * @author swx
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-06-02 15:34:56
 */
public interface UserService extends IService<User> {

    /**
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword,String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户参数
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param user 脱敏前用户
     * @return 脱敏后用户
     */
    User getSafeUser(User user);

    /**
     * 用户注销,移除登录态
     * @return 返回1表示注销成功
     */
    public int userLogout(HttpServletRequest request);

    /**
     * 获取当前用户（脱敏后）
     * @param request
     * @return 脱敏后用户
     */
    public User getCurrentUser(HttpServletRequest request);
}
