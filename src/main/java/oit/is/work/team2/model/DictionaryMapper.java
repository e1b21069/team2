package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DictionaryMapper {

    @Select("select * from dictionary")
    ArrayList<Dictionary> selectAll();

    @Select("select * from dictionary where id = #{id}")
    Dictionary selectById(Integer id);

    @Select("select * from dictionary where word = #{word}")
    Dictionary selectByWord(String word);

    @Delete("delete from dictionary where id =#{id}")
    boolean deleteById(int id);

    @Update("update dictionary set word = #{word} where id = #{id}")
    boolean updateById(Dictionary dictionary);

    @Insert("insert into dictionary (word) values (#{word})")
    boolean insert(String word);

}
