package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RoomMapper {

    @Select("SELECT * FROM ROOMS")
    ArrayList<Room> selectAll();

    @Select("SELECT * FROM ROOMS WHERE ID = #{id}")
    Room selectById(int id);

    @Select("SELECT * FROM ROOMS WHERE NAME = #{name}")
    Room selectByName(String name);

    @Delete("DELETE FROM ROOMS WHERE ID = #{id}")
    void deleteById(int id);

    @Update("UPDATE ROOMS SET NAME = #{name} WHERE ID = #{id}")
    void updateById(Room room);

    @Insert("INSERT INTO ROOMS (NAME) VALUES (#{name})")
    void insert(String name);
   
}
