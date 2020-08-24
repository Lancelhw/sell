package com.example.sell.service;

import com.example.sell.dto.CartDto;
import com.example.sell.pojo.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IProductService {

    ProductInfo findOne(String productId);

    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //加库存
    void increaseStock(List<CartDto> cartDtoList);

    //减库存
    void decreaseStock(List<CartDto> cartDtoList);
}
