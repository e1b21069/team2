package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WordLogMapper {
  @Select("select * from wordLog")
  ArrayList<WordLog> selectAll();

  @Select("select count(*) from wordLog")
  int dataCount();

  @Select("select  from wordLog desc limit 1")
  String selectAns();

  @Insert("insert into wordLog (ans, eatcnt, bitecnt) values (#{ans}, #{eatcnt}, #{bitecnt})")
  boolean insert(String ans, int eatcnt, int bitecnt);
}
