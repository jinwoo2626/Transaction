package com.example.DBridge2.mapper;

import com.example.DBridge2.DTO.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT user_email FROM USER WHERE user_email = #{user_email}")
    String findByEmail(String email);

    @Select("SELECT username FROM users WHERE username = #{username}")
    String check_duplicate(String username);

    @Select("SELECT username FROM users WHERE user_id = #{user_id}")
    String getusername(int user_id);

    @Select("SELECT userpw FROM USER WHERE userid = #{userid}")
    String getpw(String user);

    @Select("select * from user")
    UserDTO all();

    @Select("select user_id from users where username = #{username}")
    Long getuserid(String username);

    @Select("select nickname from users where username = #{username}")
    String getusernickname(String username);

    @Select("select nickname from users where user_id =#{user_id}")
    String getnickname(Long user_id);   //게시판 리스트 불러오기 / 회원 이름

}
