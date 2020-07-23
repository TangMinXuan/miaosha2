package com.tmx.miaosha2.DAO;

import com.tmx.miaosha2.DAO.DO.OrderDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDAO {

    @Select("select * from miaosha_order where order_id = #{orderId}")
    OrderDO getOrderById(@Param("orderId") Long orderId);

    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    OrderDO getOderByUserIdGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);

    @Insert("insert into miaosha_order(user_id, goods_id, status) values (#{userId}, #{goodsId}, 0)")
    Long createOrder(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
}
