package com.example.demoShop.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoShop.dto.OrderDTO;

@Mapper
public interface OrderMapper {
    int insertOrder(OrderDTO orderDTO);
    OrderDTO selectOrderByNo(int no);
    OrderDTO selectOrderByOrderId(String orderId);
    List<OrderDTO> selectOrdersByMemberNo(int memberNo);
    List<OrderDTO> selectAllOrders();
    List<OrderDTO> selectOrdersByState(String state);
    List<OrderDTO> searchOrders(Map<String, String> params);
    int updateOrderState(Map<String, Object> params);
    int updateOrderDelivery(OrderDTO orderDTO);
    int deleteOrder(int no);
}
