package com.jingjun.dao;

import com.jingjun.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 */
@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    User getUserById(@Param("id") Integer id);

    @Insert("insert into user(id,name)values(#{id},#{name})")
    Integer insertUser(User user);
}
