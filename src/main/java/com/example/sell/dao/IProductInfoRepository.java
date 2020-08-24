package com.example.sell.dao;

import com.example.sell.pojo.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IProductInfoRepository extends JpaRepository<ProductInfo,String> {
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
