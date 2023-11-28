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

    @Select("SELECT id, name from users where roomId = #{roomId}")
    User selectByRoomId(int roomId);

    // roomIdのuserの数を取得
    @Select("SELECT COUNT(*) FROM users WHERE roomId = #{roomId}")
    int selectCountByRoomId(int roomId);

    @Insert("INSERT INTO users (roomId, name) VALUES (#{roomId}, #{name})")
    void insert(int roomId, String name);

    @Select("SELECT id, name from users")
    ArrayList<User> selectAll();
}