package com.example.sell.vo;

import lombok.Data;

@Data
public class ResultVo<T> {

    /*错误码*/
    private Integer code;

    /*提示消息*/
    private String msg;

    /*返回数据*/
    private T data;
}
