package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoomMapper {
    @Select("SELECT * FROM ROOMS WHERE ID = #{id}")
    Room selectById(int id);

    @Select("SELECT * FROM ROOMS WHERE NAME = #{name}")
    Room selectByName(String name);

    @Insert("INSERT INTO ROOMS (NAME) VALUES (#{name})")
    void insert(String name);

    @Select("SELECT * FROM ROOMS")
    ArrayList<Room> selectAll();    
}
