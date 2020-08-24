package com.example.sell.controller;


import com.example.sell.pojo.ProductCategory;
import com.example.sell.pojo.ProductInfo;
import com.example.sell.service.ICategoryService;
import com.example.sell.service.IProductService;
import com.example.sell.utils.ResultVoUtil;
import com.example.sell.vo.ProductInfoVo;
import com.example.sell.vo.ProductVo;
import com.example.sell.vo.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sell/buyer/product")
public class BuyerProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("list")
    @Cacheable(cacheNames = "product",key = "pd")
    public ResultVo list(){
        /*查询所有上架的商品*/
        List<ProductInfo> productInfoList=productService.findUpAll();
        /*查询类目(一次性查询)*/
        List<Integer> categoryTypeList=new ArrayList<>();
        for (ProductInfo productInfo:productInfoList){
            categoryTypeList.add(productInfo.getCategoryType());
        }
        List<ProductCategory> productCategoryList=categoryService.findByCategoryType(categoryTypeList);
        /*数据拼装*/
        List<ProductVo> productVoList=new ArrayList<>();
        for (ProductCategory productCategory:productCategoryList){
            ProductVo productVo=new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVo> productInfoVoList=new ArrayList<>();
            for (ProductInfo productInfo:productInfoList ){
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVo productInfoVo=new ProductInfoVo();
                    BeanUtils.copyProperties(productInfoVo,productInfoVo);
                    productInfoVoList.add(productInfoVo);
                }
            }
            productVo.setProductInfoVoList(productInfoVoList);
            productVoList.add(productVo);
        }
        return ResultVoUtil.success(productVoList);
    }
}
