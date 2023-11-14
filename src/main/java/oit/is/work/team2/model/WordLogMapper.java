package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface WordLogMapper {
  @Select("select * from wordLog")
  ArrayList<WordLog> selectAll();

  @Insert("insert into wordLog (ans, eatcnt, bitecnt) values (#{ans}, #{eatcnt}, #{bitecnt})")
  boolean insert(String ans, int eatcnt, int bitecnt);
}
