package com.myshop.service;

import com.myshop.common.ServerResponse;
import com.myshop.pojo.User;

/**
 * Created by chs on 2017-10-13.
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str,String type);
    ServerResponse<String> selectQuestion(String username);
    ServerResponse<String> checkForgetAnswer(String username,String question,String answer);
    ServerResponse<String> forgerResetPassword(String username,String newPassword,String forgetToken);
    ServerResponse<String> resetPassword( String oldPassword, String newPassword ,User user);
    ServerResponse<User> updateInformation(User user);
    ServerResponse<User> getInformation(int userId);
}
