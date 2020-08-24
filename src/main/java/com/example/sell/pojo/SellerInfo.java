package com.example.sell.pojo;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class SellerInfo {

    @Id
    private String sellerId;

    private String userName;

    private String passWord;

    private String openId;
}
