package com.tmx.miaosha2.DAO;

import com.tmx.miaosha2.DAO.DO.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDAO {

    @Select("select * from user where user_id = #{userId}")
    UserDO getUserById(@Param("userId") Long userId);

    @Select("select * from user where user_Email = #{userEmail}")
    UserDO getUserByEmail(@Param("userEmail") String userEmail);
}
