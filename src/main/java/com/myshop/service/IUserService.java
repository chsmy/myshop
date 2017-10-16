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
}
