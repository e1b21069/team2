package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT id, name FROM users WHERE id = #{id}")
    User selectById(String id);

    @Select("SELECT id, name FROM users WHERE name = #{name}")
    User selectByName(String name);

    @Select("SELECT id, name FROM users WHERE roomId = #{roomId}")
    User selectByRoomId(int roomId);

    // idからroomIdを取得
    @Select("SELECT roomId FROM users WHERE id = #{id}")
    int selectRoomIdById(int id);

    @Select("SELECT COUNT(*) FROM users WHERE roomId = #{roomId}")
    int selectCountByRoomId(int roomId);

    @Insert("INSERT INTO users (name) VALUES (#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    @Insert("INSERT INTO users (roomId, name) VALUES (#{roomId}, #{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertWithRoomId(User user);

    @Select("SELECT id FROM users WHERE name = #{name}")
    int selectIdByName(String name);

    @Insert("INSERT INTO users (roomId, name) VALUES (#{roomId}, #{name})")
    void insert(int roomId, String name);

    @Select("SELECT id, name from users")
    ArrayList<User> selectAll();
}