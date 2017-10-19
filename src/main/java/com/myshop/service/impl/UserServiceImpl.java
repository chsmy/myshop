package com.myshop.service.impl;

import com.myshop.common.Const;
import com.myshop.common.ServerResponse;
import com.myshop.common.TokenCache;
import com.myshop.dao.UserMapper;
import com.myshop.pojo.User;
import com.myshop.service.IUserService;
import com.myshop.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.UUID;

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

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功",user);
    }

    public ServerResponse<String> register(User user){
        ServerResponse<String> validServerResponse = checkValid(user.getUsername(), Const.USERNAME);
        if(!validServerResponse.isSuccess()){
            return validServerResponse;
        }
        validServerResponse = checkValid(user.getEmail(), Const.EMAIL);
        if(!validServerResponse.isSuccess()){
            return validServerResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOM);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);

        if(resultCount == 0){
         return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String str,String type){
          if(StringUtils.isNotBlank(type)){
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

    public ServerResponse<String> selectQuestion(String username){
        ServerResponse<String> validServerResponse = this.checkValid(username, Const.USERNAME);
        if(validServerResponse.isSuccess()){
           //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
         return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题为空");
    }

    public ServerResponse<String> checkForgetAnswer(String username,String question,String answer){
     int resCode = userMapper.checkForgetAnswer(username,question,answer);
        if(resCode > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_HEAD+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }
    public ServerResponse<String> forgerResetPassword(String username,String newPassword,String forgetToken){
       if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，token不能为空");
       }
        ServerResponse<String> validServerResponse = this.checkValid(username, Const.USERNAME);
        if(validServerResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_HEAD + username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if(StringUtils.equals(forgetToken,token)){
         String md5password = MD5Util.MD5EncodeUtf8(newPassword);
            int resCode = userMapper.updatePasswordByUsername(username,md5password);
            if(resCode > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else {
            ServerResponse.createByErrorMessage("token错误，请从新获取");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }
    public ServerResponse<String> resetPassword( String oldPassword, String newPassword ,User user){
      int resCode = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
        if(resCode == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));

        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
           return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }
    public ServerResponse<User> updateInformation(User user){
      int resCount = userMapper.checkEmailByUser(user.getEmail(),user.getId());
        if(resCount > 0 ){
            ServerResponse.createByErrorMessage("email已经存在");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount>0){
           return ServerResponse.createBySuccessMessage("更新个人信息成功");
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }
    public ServerResponse<User> getInformation(int userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
}
