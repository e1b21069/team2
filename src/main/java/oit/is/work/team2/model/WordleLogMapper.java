package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WordleLogMapper {
  @Select("select * from wordleLog")
  ArrayList<WordleLog> selectAll();

  // roomIdを指定して検索
  @Select("select * from wordleLog where roomId = #{roomId}")
  ArrayList<WordleLog> selectAllByRoomId(int roomId);

  @Select("select * from wordleLog where userId = #{userId}")
  ArrayList<WordleLog> selectAllByUserId(int userId);

  @Select("select count(*) from wordleLog where userId = #{userId}")
  int dataCount(int userId);

  @Select("select ans from wordleLog order by id desc limit 1")
  String selectAns();

  @Insert("insert into wordleLog (ans, result) values (#{ans}, #{result})")
  boolean insert(String ans, int result);

  @Insert("insert into wordleLog (roomId, ans, result) values (#{roomId}, #{ans}, #{result})")
  boolean insertMulti(int roomId, String ans, int result);

  @Insert("insert into wordleLog (ans, userId, result) values (#{ans}, #{userId}, #{result})")
  boolean insertWithUserId(String ans, int userId, int result);

  // userIdを指定して削除
  @Delete("delete from wordleLog where userId = #{userId}")
  boolean deleteByUserId(int userId);

  // roomIdを指定して削除
  @Delete("delete from wordleLog where roomId = #{roomId}")
  boolean deleteByRoomId(int roomId);
}
