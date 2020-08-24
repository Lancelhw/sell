package com.example.sell.converter;

import com.example.sell.dto.OrderDto;
import com.example.sell.pojo.OrderMaster;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.stream.Collectors;


public class OrderMasterToOrderDtoConverter {

    public static OrderDto convert(OrderDto orderMaster) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster, orderDto);
        return orderDto;
    }

   public static List<OrderDto> convert(List<OrderMaster> orderMasterList) {
       return orderMasterList.stream().map(e -> convert(e).collect(Collectors.toList()));
    }

}
