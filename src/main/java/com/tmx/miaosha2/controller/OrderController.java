package com.tmx.miaosha2.controller;

import com.tmx.miaosha2.DAO.DO.GoodsDO;
import com.tmx.miaosha2.DAO.DO.OrderDO;
import com.tmx.miaosha2.DAO.DO.UserDO;
import com.tmx.miaosha2.controller.VO.OrderDetailVO;
import com.tmx.miaosha2.result.ErrorMessage;
import com.tmx.miaosha2.result.GlobalException;
import com.tmx.miaosha2.result.Result;
import com.tmx.miaosha2.service.GoodsService;
import com.tmx.miaosha2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;
    
    @GetMapping("/detail")
    public Result<OrderDetailVO> getOrderDetail(@RequestParam("orderId") Long orderId,
                                                UserDO user) {
        if(user == null) {
            throw new GlobalException(ErrorMessage.TOKEN_ERROR);
        }

        OrderDO order = orderService.getOrderById(orderId);
        if(order == null) {
            throw new GlobalException(ErrorMessage.ORDER_NOT_EXIST);
        }
        GoodsDO goods = goodsService.getGoodsById(order.getGoodsId());
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setOrder(order);
        orderDetailVO.setGoods(goods);
        return Result.success(orderDetailVO);
    }
}
