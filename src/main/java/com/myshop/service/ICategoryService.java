package com.myshop.service;

import com.myshop.common.ServerResponse;
import com.myshop.pojo.Category;

import java.util.List;

/**
 * Created by chs on 2017-10-20.
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName , Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId,String categoryName);
    ServerResponse<List<Category>> getCategoryList(Integer categoryId);
    ServerResponse<List<Integer>> getDeepCategoryList(Integer categoryId);
}
