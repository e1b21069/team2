package oit.is.work.team2.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DictionaryMapper {

    @Select("SELECT * from dictionary where id = #{id}")
    Dictionary selectByIdDictionary(String id);

    @Select("SELECT * from dictionary where word = #{word}")
    Dictionary selectByWordDictionary(String word);

    @Select("SELECT * from dictionary")
    ArrayList<Dictionary> selectAllDictionary();

}
