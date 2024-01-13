package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface MatchMapper {

    @Select("select * from match")
    ArrayList<Match> selectAll();

    @Select("select * from match where id = #{id}")
    Match selectById(int id);

    @Select("select word from match where roomId = #{roomId}")
    String selectWord(int roomId);

    @Insert("insert into match (roomId, word) values (#{roomId}, #{word})")
    boolean insert(int roomId, String word);

    // roomIdを指定して、その部屋のマッチング情報を削除する
    @Delete("delete from match where roomId = #{roomId}")
    boolean deleteByRoomId(int roomId);
}
