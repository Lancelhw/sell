package com.example.sell.utils;

import com.example.sell.vo.ResultVo;

public class ResultVoUtil {
    public static ResultVo success(Object object){
        ResultVo resultVo=new ResultVo();
        /*ProductVo productVo=new ProductVo();
        ProductInfoVo productInfoVo=new ProductInfoVo();
        *//*嵌套添加返回字段*//*
        productVo.setProductInfoVoList(Arrays.asList(productInfoVo));*/
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        return resultVo;
    }

    public static ResultVo success(){
        return success(null);
    }

    public static ResultVo error(Integer code,String msg){
        ResultVo resultVo=new ResultVo();
        resultVo.setCode(code);
        resultVo.setMsg(msg);
        return resultVo;
    }
}
