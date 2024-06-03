package org.swx.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.swx.usercenter.Constant.UserConstant;
import org.swx.usercenter.common.BaseResponse;
import org.swx.usercenter.common.ErrorCode;
import org.swx.usercenter.common.ResponseUtils;
import org.swx.usercenter.exception.BusinessException;
import org.swx.usercenter.model.User;
import org.swx.usercenter.model.request.UserLoginRequest;
import org.swx.usercenter.model.request.UserRegisterRequest;
import org.swx.usercenter.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.swx.usercenter.Constant.UserConstant.ADMIN_ROLE;
import static org.swx.usercenter.Constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author swx
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest==null){
            /*return ResponseUtils.errot(ErrorCode.PARAMS_ERROR);*/
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数有空值");
        }
        long res = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResponseUtils.success(res);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if(userLoginRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数有空值");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResponseUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if(request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }

        int res = userService.userLogout(request);
        return ResponseUtils.success(res);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String userName, HttpServletRequest request) {
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"没有管理员权限");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(userName)){
            queryWrapper.like("username",userName);
        }
        List<User> list = userService.list(queryWrapper);
        return ResponseUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> userDelete(long id, HttpServletRequest request) {
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"没有管理员权限");
        }
        if(id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数id<=0");
        }
        boolean res = userService.removeById(id);
        return ResponseUtils.success(res);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User currentUser = userService.getCurrentUser(request);
        return ResponseUtils.success(currentUser);
    }

    /**
     *是否为管理员
     * @param request HTTP请求
     * @return 返回true
     */
    private Boolean isAdmin(HttpServletRequest request){
        //仅管理员可操作
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if(user==null || user.getRole()!=ADMIN_ROLE){
            return false;
        }
        return true;
    }


}

