package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchMapper {

    @Select("select * from match")
    ArrayList<Match> selectAll();

    @Select("select * from match where id = #{id}")
    Match selectById(Integer id);

    @Select("select word from match where id = #{id}")
    String selectWord(Integer id);

    @Update("update match set firstWin = #{firstWin} where id = #{id}")
    boolean updateById(Match match);

    @Insert("insert into match (word, player1, player2) values (#{word}, #{player1}, #{player2})")
    boolean insert(String word, Integer player1, Integer player2);

}
