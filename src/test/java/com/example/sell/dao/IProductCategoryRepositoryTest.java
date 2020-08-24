package com.example.sell.dao;

import com.example.sell.pojo.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IProductCategoryRepositoryTest {

    @Autowired
    private IProductCategoryRepository IProductCategoryRepository;

    @Test
    public void TestOne(){
        ProductCategory productCategory = IProductCategoryRepository.getOne(1);
        System.out.println(productCategory.toString());
    }

    @Test
    @Transactional
    public void TestSave(){
        ProductCategory productCategory=new ProductCategory();
        productCategory.setCategoryName("家居用品");
        productCategory.setCategoryType(2);
        IProductCategoryRepository.save(productCategory);
    }

   /* @Test
    public void TestFindByCategoryType(){
        List<Integer> list= Arrays.asList(1);
        List<ProductCategory> result= IProductCategoryRepository.findByCategoryType(list);
        Assert.assertNotEquals(0,result.size());
    }*/


}