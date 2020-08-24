package com.example.sell.service;

import com.example.sell.pojo.ProductCategory;
import java.util.List;

public interface ICategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryType(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
