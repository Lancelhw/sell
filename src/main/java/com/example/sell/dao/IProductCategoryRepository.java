package com.example.sell.dao;

import com.example.sell.pojo.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {

    List<ProductCategory> findByCategoryType(List<Integer> categoryTypeList);
}
