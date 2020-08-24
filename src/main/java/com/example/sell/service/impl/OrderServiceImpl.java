package com.example.sell.service.impl;

import com.example.sell.converter.OrderMasterToOrderDtoConverter;
import com.example.sell.dao.IOrderDetailRepository;
import com.example.sell.dao.IOrderMasterRepository;
import com.example.sell.dto.CartDto;
import com.example.sell.dto.OrderDto;
import com.example.sell.enums.OrderStatusEnum;
import com.example.sell.enums.PayStatusEnum;
import com.example.sell.enums.ResultEnum;
import com.example.sell.exception.SellException;
import com.example.sell.pojo.OrderDetail;
import com.example.sell.pojo.OrderMaster;
import com.example.sell.pojo.ProductInfo;
import com.example.sell.service.IOrderService;
import com.example.sell.service.IProductService;
import com.example.sell.service.WebSocket;
import com.example.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IProductService productService;

    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    @Autowired
    private IOrderMasterRepository orderMasterRepository;

    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {

        String orderId=KeyUtil.genUniqueKey();
        BigDecimal orderAmount=new BigDecimal(BigInteger.ZERO);
        List<CartDto> cartDtoList=new ArrayList<>();

        /*1.查询商品*/
        for (OrderDetail orderDetail:orderDto.getOrderDetailList()){
            ProductInfo productInfo=productService.findOne(orderDetail.getProductId());
            if (productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            /*2.计算订单总价*/
            orderAmount=orderDetail.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);
            /*订单详情入库*/
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            //属性拷贝BeanUtils
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);
            //设置库存
            CartDto cartDto=new CartDto(orderDetail.getProductId(),orderDetail.getProductQuantity());
            cartDtoList.add(cartDto);
        }
        /*3.写入订单数据库*/
        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderDto,orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        /*4.减库存*/
        productService.decreaseStock(cartDtoList);
        //发生websocket消息
        webSocket.sendMessage("订单号:"+orderDto.getOrderId());
        return orderDto;
    }

    @Override
    public OrderDto findOne(String orderId) {
        OrderMaster orderMaster=orderMasterRepository.getOne(orderId);
        if (orderMaster  == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList=orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        OrderDto orderDto=new OrderDto();
        BeanUtils.copyProperties(orderMaster,orderDto);
        orderDto.setOrderDetailList(orderDetailList);
        return orderDto;
    }

    @Override
    public Page<OrderDto> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage= orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDto> orderDtoList=OrderMasterToOrderDtoConverter.convert(orderMasterPage.getContent());
        Page<OrderDto> orderDtoPage=new PageImpl<>(orderDtoList,pageable,orderMasterPage.getTotalElements());
        return orderDtoPage;
    }

    @Override
    @Transactional
    public OrderDto cancel(OrderDto orderDto) {
        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态
        if (!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}", orderDto.getOrderId(), orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDto.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDto, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //返回库存
        if (CollectionUtils.isEmpty(orderDto.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDto);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDto> cartDTOList = orderDto.getOrderDetailList().stream()
                .map(e -> new CartDto(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);

        //如果已支付, 需要退款
        if (orderDto.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            //TODO
            /*payService.refund(orderDTO);*/
        }
        return orderDto;
    }

    @Override
    public OrderDto finish(OrderDto orderDTO) {
        return null;
    }

    @Override
    public OrderDto paid(OrderDto orderDTO) {
        return null;
    }

    @Override
    public Page<OrderDto> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage=orderMasterRepository.findAll(pageable);
        List<OrderDto> orderDtoList= OrderMasterToOrderDtoConverter.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDtoList, pageable, orderMasterPage.getTotalElements());
    }
}
