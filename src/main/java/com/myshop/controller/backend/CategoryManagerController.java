package com.myshop.controller.backend;

import com.myshop.common.Const;
import com.myshop.common.ResponseCode;
import com.myshop.common.ServerResponse;
import com.myshop.pojo.Category;
import com.myshop.pojo.User;
import com.myshop.service.ICategoryService;
import com.myshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by chs on 2017-10-20.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManagerController {

    @Autowired
    IUserService iUserService;

    @Autowired
    ICategoryService iCategoryService;

    /**
     * 添加品类
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆需要登陆");
        }
        //校验是不是管理员
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    /**
     * 更改品类的名字
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆需要登陆");
        }
        //校验是不是管理员
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    /**
     * 获取商品种类的list  只获得一级不递归
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_category_list.do")
    @ResponseBody
    public ServerResponse<List<Category>> getCategoryList(HttpSession session,  @RequestParam(value = "categoryId", defaultValue = "0")Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆需要登陆");
        }
        //校验是不是管理员
        if (iUserService.checkAdmin(user).isSuccess()) {
           return iCategoryService.getCategoryList(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
    /**
     * 获取商品种类的list  递归获取
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_deep_category_list.do")
    @ResponseBody
    public ServerResponse getDeepCategoryList(HttpSession session,  @RequestParam(value = "categoryId", defaultValue = "0")Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆需要登陆");
        }
        //校验是不是管理员
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iCategoryService.getDeepCategoryList(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
}
