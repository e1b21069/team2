package oit.is.work.team2.model;

import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface MatchInfoMapper {
    @Insert("INSERT INTO MATCHINFO (USER1, USER2, ISACTIVE)  VALUES (#{user1}, #{user2}, #{isActive})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    boolean insertMatchInfo(MatchInfo matchInfo);

    @Select("SELECT * FROM MATCHINFO WHERE ISACTIVE = TRUE")
    ArrayList<MatchInfo> selectActiveMatches();

    @Select("SELECT * FROM MATCHINFO WHERE (USER1 = #{user} or USER2=#{user}) and ISACTIVE = TRUE")
    MatchInfo selectActiveMatchInfoByUser(int user);

    @Update("UPDATE MATCHINFO SET ISACTIVE = #{isActive} WHERE ID = #{id}")
    boolean updateMatchInfo(MatchInfo matchinfo);
}