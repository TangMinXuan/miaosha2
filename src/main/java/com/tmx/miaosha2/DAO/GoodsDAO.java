package com.tmx.miaosha2.DAO;

import com.tmx.miaosha2.DAO.DO.GoodsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDAO {

    @Select("select * from goods")
    List<GoodsDO> getGoodsList();

    @Select("select * from goods where goods_id = #{goodsId}")
    GoodsDO getGoodsById(@Param("goodsId") Long goodsId);

    @Update("update goods set goods_stock = goods_stock - 1 where goods_id = #{goodsId} and goods_stock > 0")
    int reduceStock(@Param("goodsId") Long goodsId);
}
