package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface WordLogMapper {
  @Select("select * from wordLog")
  ArrayList<WordLog> selectAll();

  @Select("select * from wordLog where userId = #{userId}")
  ArrayList<WordLog> selectAllByUserId(int userId);

  @Select("select count(*) from wordLog")
  int dataCount();

  @Select("select ans from wordLog order by id desc limit 1")
  String selectAns();

  @Insert("insert into wordLog (ans, eatcnt, bitecnt) values (#{ans}, #{eatcnt}, #{bitecnt})")
  boolean insert(String ans, int eatcnt, int bitecnt);

  @Insert("insert into wordLog (roomId, ans, eatcnt, bitecnt) values (#{roomId}, #{ans}, #{eatcnt}, #{bitecnt})")
  boolean insertMulti(int roomId, String ans, int eatcnt, int bitecnt);

  @Insert("insert into wordLog (ans, userId, eatcnt, bitecnt) values (#{ans}, #{userId}, #{eatcnt}, #{bitecnt})")
  boolean insertWithUserId(String ans, int userId, int eatcnt, int bitecnt);

  // userIdを指定して削除
  @Delete("delete from wordLog where userId = #{userId}")
  boolean deleteByUserId(int userId);

  // roomIdを指定して削除
  @Delete("delete from wordLog where roomId = #{roomId}")
  boolean deleteByRoomId(int roomId);
}
