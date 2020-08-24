package com.example.sell.service;

import com.example.sell.pojo.SellerInfo;

public interface ISellerService {

    SellerInfo findSellerInfoByOpenId(String openId);
}
