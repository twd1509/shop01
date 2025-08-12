package com.example.demoShop.mapper;

import java.util.List;

import com.example.demoShop.dto.OrderCancelDTO;

public interface OrderCancelMapper {
	int insertOrderCancel(OrderCancelDTO orderCancelDTO);
    OrderCancelDTO selectOrderCancelByNo(int no);
    List<OrderCancelDTO> selectOrderCancelsByOrderNo(int orderNo);
    List<OrderCancelDTO> selectOrderCancelsByMemberNo(int memberNo);
    int updateOrderCancelState(OrderCancelDTO orderCancelDTO);
    int deleteOrderCancel(int no);
}
