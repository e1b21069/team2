package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT id, name from users where id = #{id}")
    User selectById(String id);

    @Select("SELECT id, name from users where name = #{name}")
    User selectByName(String name);

    @Insert("INSERT INTO users (name) VALUES (#{name})")
    void insert(String name);

    @Select("SELECT id, name from users")
    ArrayList<User> selectAll();
}