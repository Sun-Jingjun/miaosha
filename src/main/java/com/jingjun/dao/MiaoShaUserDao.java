package com.jingjun.dao;

import com.jingjun.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 *
 */
@Mapper
public interface MiaoShaUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    public MiaoshaUser getUserById(@Param("id") Long id);

    @Update("update miaosha_user set password=#{password} where id=#{id}")
    void update(MiaoshaUser miaoshaUser);
}
