package com.myshop.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.myshop.common.ServerResponse;
import com.myshop.dao.CategoryMapper;
import com.myshop.pojo.Category;
import com.myshop.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

/**
 * Created by chs on 2017-10-20.
 */
@Service("iCategoryService")
public class ICategoryServiceImpl implements ICategoryService {
    private Logger logger = LoggerFactory.getLogger(ICategoryServiceImpl.class);
    @Autowired
    CategoryMapper categoryMapper;

    public ServerResponse addCategory(String categoryName , Integer parentId){
      if(parentId == null || StringUtils.isBlank(categoryName)){
        return ServerResponse.createByErrorMessage("参数错误无法添加");
      }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        int insertCount = categoryMapper.insert(category);
        if(insertCount>0){
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("参数错误无法添加");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int updateCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("修改品类名成功");
        }
        return ServerResponse.createByErrorMessage("修改品类名失败");
    }

    public ServerResponse<List<Category>> getCategoryList(Integer categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
          logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }
    /**
     * 递归查询本节点的id及孩子节点的id
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> getDeepCategoryList(Integer categoryId){
       Set<Category> categorySet = Sets.newHashSet();
        findChildrenCategory(categorySet,categoryId);
        List<Integer> categoryList = Lists.newArrayList();
        for (Category c : categorySet) {
            categoryList.add(c.getId());
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    private Set<Category> findChildrenCategory(Set<Category> categorySet,Integer categoryId){
      Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category !=null){
             categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category c : categoryList) {
            findChildrenCategory(categorySet,c.getId());
        }
        return categorySet;
    }

}
