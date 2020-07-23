package com.tmx.miaosha2.controller.VO;

import com.tmx.miaosha2.DAO.DO.GoodsDO;
import com.tmx.miaosha2.DAO.DO.OrderDO;

public class OrderDetailVO {

    private OrderDO order;
    private GoodsDO goods;

    public OrderDO getOrder() {
        return order;
    }

    public void setOrder(OrderDO order) {
        this.order = order;
    }

    public GoodsDO getGoods() {
        return goods;
    }

    public void setGoods(GoodsDO goods) {
        this.goods = goods;
    }
}
