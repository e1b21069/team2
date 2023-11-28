package oit.is.work.team2.model;

import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface MatchInfoMapper {
    @Select("SELECT * FROM MATCHINFO WHERE ISACTIVE = TRUE")
    ArrayList<MatchInfo> selectActiveMatches();

    @Select("SELECT * FROM MATCHINFO WHERE ID = #{id}")
    MatchInfo selectMatchInfoById(int id);

    @Select("SELECT * FROM MATCHINFO WHERE ROOMNAME = #{roomName}")
    MatchInfo selectMatchInfoByRoomName(String roomName);

    @Select("SELECT * FROM MATCHINFO WHERE PPLNUM = #{pplNum}")
    MatchInfo selectMatchInfoByPplNum(int pplNum);

    @Select("SELECT * FROM MATCHINFO WHERE ISACTIVE = #{isActive}")
    MatchInfo selectMatchInfoByIsActive(boolean isActive);

    @Select("SELECT * FROM MATCHINFO")
    ArrayList<MatchInfo> selectAll();

    @Update("UPDATE MATCHINFO SET ROOMNAME = #{roomName} WHERE ID = #{id}")
    boolean updateMatchInfoRoomName(MatchInfo matchinfo);

    @Update("UPDATE MATCHINFO SET PPLNUM = #{pplNum} WHERE ID = #{id}")
    boolean updateMatchInfoPplNum(MatchInfo matchinfo);

    @Update("UPDATE MATCHINFO SET ISACTIVE = #{isActive} WHERE ID = #{id}")
    boolean updateMatchInfoIsActive(MatchInfo matchinfo);

    @Insert("INSERT INTO MATCHINFO (ROOMNAME, PPLNUM, ISACTIVE) VALUES (#{roomName}, #{pplNum}, #{isActive})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    boolean insertMatchInfo(MatchInfo matchinfo);

}