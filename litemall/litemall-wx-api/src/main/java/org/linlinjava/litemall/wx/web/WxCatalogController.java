package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallCategory;
import org.linlinjava.litemall.db.service.LitemallCategoryService;
import org.linlinjava.litemall.wx.service.HomeCacheManager;
import org.linlinjava.litemall.wx.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类目服务
 */
@RestController
@RequestMapping("/wx/catalog")
@Validated
public class WxCatalogController {
    private final Log logger = LogFactory.getLog(WxCatalogController.class);

    @Autowired
    private LitemallCategoryService categoryService;

    @GetMapping("/getfirstcategory")
    public Object getFirstCategory() {
        // 所有一级分类目录
        List<LitemallCategory> l1CatList = categoryService.queryL1();
        return ResponseUtil.ok(l1CatList);
    }

    @GetMapping("/getsecondcategory")
    public Object getSecondCategory(@NotNull Integer id) {
        // 所有二级分类目录
        List<LitemallCategory> currentSubCategory = categoryService.queryByPid(id);
        return ResponseUtil.ok(currentSubCategory);
    }

    /**
     * 分类详情
     *
     * @param id   分类类目ID。
     *             如果分类类目ID是空，则选择第一个分类类目。
     *             需要注意，这里分类类目是一级类目
     * @return 分类详情
     */
    @GetMapping("index")
    public Object index(Integer id) {

        // 所有一级分类目录
        List<LitemallCategory> l1CatList = categoryService.queryL1();

        // 当前一级分类目录
        LitemallCategory currentCategory = null;
        if (id != null) {
            currentCategory = categoryService.findById(id);
        } else {
             if (l1CatList.size() > 0) {
                currentCategory = l1CatList.get(0);
            }
        }

        // 当前一级分类目录对应的二级分类目录
        List<LitemallCategory> currentSubCategory = null;
        if (null != currentCategory) {
            currentSubCategory = categoryService.queryByPid(currentCategory.getId());
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("categoryList", l1CatList);
        data.put("currentCategory", currentCategory);
        data.put("currentSubCategory", currentSubCategory);
        return ResponseUtil.ok(data);
    }

    /**
     * 所有分类数据
     *
     * @return 所有分类数据
     */
    @GetMapping("all")
    public Object queryAll() {
        //优先从缓存中读取
        if (HomeCacheManager.hasData(HomeCacheManager.CATALOG)) {
            return ResponseUtil.ok(HomeCacheManager.getCacheData(HomeCacheManager.CATALOG));
        }


        // 所有一级分类目录
        List<LitemallCategory> l1CatList = categoryService.queryL1();

        //所有子分类列表
        Map<Integer, List<CategoryVo>> allList = new HashMap<>();
        for (LitemallCategory category : l1CatList) {
            //二级子目录
            List<CategoryVo> subs = new ArrayList<>();
            List<LitemallCategory> subCategoryList = categoryService.queryByPid(category.getId());
            for(LitemallCategory subCategory : subCategoryList) {
//            sub = categoryService.queryByPid(category.getId());
                CategoryVo subCategoryVo = new CategoryVo();
                subCategoryVo.setId(subCategory.getId());
                subCategoryVo.setDesc(subCategory.getDesc());
                subCategoryVo.setIconUrl(subCategory.getIconUrl());
                subCategoryVo.setPicUrl(subCategory.getPicUrl());
                subCategoryVo.setKeywords(subCategory.getKeywords());
                subCategoryVo.setName(subCategory.getName());
                subCategoryVo.setPid(subCategory.getPid());
                subCategoryVo.setLevel(subCategory.getLevel());
                //三级子目录
                List<CategoryVo> children = new ArrayList<>();
                List<LitemallCategory> thirdCategoryList = categoryService.queryByPid(subCategory.getId());
                for (LitemallCategory thirdCategory : thirdCategoryList) {
                    CategoryVo thirdCategoryVo = new CategoryVo();
                    thirdCategoryVo.setId(thirdCategory.getId());
                    thirdCategoryVo.setDesc(thirdCategory.getDesc());
                    thirdCategoryVo.setIconUrl(thirdCategory.getIconUrl());
                    thirdCategoryVo.setPicUrl(thirdCategory.getPicUrl());
                    thirdCategoryVo.setKeywords(thirdCategory.getKeywords());
                    thirdCategoryVo.setName(thirdCategory.getName());
                    thirdCategoryVo.setPid(thirdCategory.getPid());
                    thirdCategoryVo.setLevel(thirdCategory.getLevel());
                    children.add(thirdCategoryVo);
                }
                subCategoryVo.setChildren(children);

                subs.add(subCategoryVo);
            }
            allList.put(category.getId(), subs);
        }

        // 当前一级分类目录
        LitemallCategory currentCategory = l1CatList.get(0);

        // 当前一级分类目录对应的二级分类目录
        List<LitemallCategory> currentSubCategory = null;
        if (null != currentCategory) {
            currentSubCategory = categoryService.queryByPid(currentCategory.getId());
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("categoryList", l1CatList);
        data.put("allList", allList);
        data.put("currentCategory", currentCategory);
        data.put("currentSubCategory", currentSubCategory);

        //缓存数据
        HomeCacheManager.loadData(HomeCacheManager.CATALOG, data);
        return ResponseUtil.ok(data);
    }

    /**
     * 当前分类栏目
     *
     * @param id 分类类目ID
     * @return 当前分类栏目
     */
    @GetMapping("current")
    public Object current(@NotNull Integer id) {
        // 当前分类
        LitemallCategory currentCategory = categoryService.findById(id);
        if(currentCategory == null){
            return ResponseUtil.badArgumentValue();
        }
        List<LitemallCategory> currentSubCategory = categoryService.queryByPid(currentCategory.getId());

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("currentCategory", currentCategory);
        data.put("currentSubCategory", currentSubCategory);
        return ResponseUtil.ok(data);
    }
}
