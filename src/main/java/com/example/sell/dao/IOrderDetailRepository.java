package com.example.sell.dao;


import com.example.sell.pojo.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, String> {

    List<OrderDetail> findByOrderId(String orderId);
}
