package com.myshop.service.impl;

import com.myshop.common.Const;
import com.myshop.common.ServerResponse;
import com.myshop.dao.UserMapper;
import com.myshop.pojo.User;
import com.myshop.service.IUserService;
import com.myshop.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chs on 2017-10-13.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        // todo 密码Md5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功",user);
    }

    public ServerResponse<String> register(User user){
        int resultCount = userMapper.checkUsername(user.getUsername());
        if(resultCount > 0){
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
        resultCount = userMapper.checkEmail(user.getEmail());
        if(resultCount > 0){
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }
        user.setRole(Const.Role.ROLE_CUSTOM);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        resultCount = userMapper.insert(user);

        if(resultCount == 0){
         return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String str,String type){
          if(StringUtils.isBlank(type)){
              if(Const.USERNAME.equals(type)){
                  int resultCount = userMapper.checkUsername(str);
                  if(resultCount > 0){
                      return ServerResponse.createByErrorMessage("用户名已存在");
                  }
              }
              if(Const.EMAIL.equals(type)){
                  int resultCount = userMapper.checkEmail(str);
                  if(resultCount > 0){
                      return ServerResponse.createByErrorMessage("邮箱已存在");
                  }
              }
          }else {
              return ServerResponse.createByErrorMessage("参数错误");
          }
        return ServerResponse.createBySuccess("校验成功");
    }
}
