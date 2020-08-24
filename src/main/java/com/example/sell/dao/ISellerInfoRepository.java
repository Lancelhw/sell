package com.example.sell.dao;

import com.example.sell.pojo.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISellerInfoRepository extends JpaRepository<SellerInfo,String > {

    SellerInfo findByOpenId(String openId);
}
