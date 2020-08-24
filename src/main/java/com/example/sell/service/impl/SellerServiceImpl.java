package com.example.sell.service.impl;

import com.example.sell.dao.ISellerInfoRepository;
import com.example.sell.pojo.SellerInfo;
import com.example.sell.service.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements ISellerService {

    @Autowired
    private ISellerInfoRepository sellerInfoRepository;

    @Override
    public SellerInfo findSellerInfoByOpenId(String openId) {
        return findSellerInfoByOpenId(openId);
    }
}
