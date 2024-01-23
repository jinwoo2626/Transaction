package com.example.DBridge2.mapper;

import com.example.DBridge2.DTO.*;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface BoardMapper {

//    @Select("SELECT board_content FROM board WHERE user_id = #{user}")
//    String getpw(String user);

    @Select("SELECT COUNT(*) FROM board;")
    int selectboardcnt();   //총 게시물 개수

    @Select("select * from board ORDER BY BOARDNUM DESC LIMIT #{pageNo}, #{numOfRows}")
    ArrayList<BoardListDTO> selectboard(@Param("pageNo") int pageNo, @Param("numOfRows") int numOfRows); //게시판 리스트 불러오기

    @Select("select * from board WHERE boardnum = #{BOARDNUM}")
    ArrayList<BoardListDTO> selectboardinfo(int boardnum); //상품 상세정보 내용 가져오기

    @Select("select filenamef from file where boardnum = #{BOARDNUM} LIMIT 1")
    String selectimg(int boardnum); //이미지(파일) 경로

    @Select("select filetype from file where boardnum = #{BOARDNUM} LIMIT 1")
    String selectimgtype(int boardnum); //이미지(파일) 타입

    @Select("select filename, filenamef, filetype, filenum from file where boardnum = #{BOARDNUM}")
    ArrayList<FileDTO> selectimginfo(int boardnum); //상품 상세정보 이미지(파일) 정보 가져오기

    @Insert("insert into comment values (null, #{COMMENTCONTENT}, #{BOARDNUM}, #{USERID})")
    void addcomment(COMMENTDTO commentdto);   //댓글등록

    @Select("SELECT comment.*, users.nickname FROM DBridge_Store.comment JOIN DBridge_Store.users ON comment.USERID = users.user_id WHERE comment.BOARDNUM = #{BOARDNUM}")
    ArrayList<COMMENTDTO> getcomment(int BOARDNUM); //댓글 조회

    @Delete("delete from comment where COMMENTNUM = #{COMMENTNUM}")
    void deletecomment(int COMMENTNUM); //댓글 삭제

    @Insert("insert into board values (null, #{BOARDSUB}, #{BOARDCONTENT}, #{BOARDUPTIME}, #{USERID}, #{PRODUCTINFO}, #{PRODUCTPRICE}, #{PRODUCTCOUNT}, #{PRODUCTSTATUS})")
    void addboard(BoardDTO boardDTO);   //상품등록

    @Update("UPDATE board SET BOARDSUB = #{BOARDSUB}, BOARDCONTENT = #{BOARDCONTENT}, BOARDUPTIME = #{BOARDUPTIME}, USERID = #{USERID}, PRODUCTINFO = #{PRODUCTINFO},PRODUCTPRICE = #{PRODUCTPRICE}, PRODUCTCOUNT = #{PRODUCTCOUNT}, PRODUCTSTATUS = #{PRODUCTSTATUS} WHERE BOARDNUM = #{BOARDNUM}")
    void updateBoard(BoardDTO boardDTO);    //상품수정

    @Select("select boardnum from board where userid = #{USERID} order by boardnum desc limit 1")
    int getboardnum(String userid); //최근에 등록된 게시글(상품게시글) 번호

    @Insert("insert into file values (null, #{FILENAME}, #{FILENAMEF}, #{FILETYPE}, #{FILEPATH}, #{BOARDNUM})")
    void addfile(FileDTO fileDTO);  //업로드 파일 정보 저장

    @Select("select filepath from file where filenum = #{FILENUM}")
    ArrayList<FileDTO> selectdeleteimginfo(int filenum); //삭제할 파일 정보 불러오기

    @Delete("delete from file where filenum = #{FILENUM}")
    boolean deleteimg(int filenum); //이미지(파일) 삭제

    @Select("select * from account where USERID = #{USERID}")
    ACCOUNTDTO selectaccount(int userid); //회원의 계좌데이터 정보

    @Insert("insert into reservation values (null, #{BOARDNUM}, #{USERID}, #{HOSTUSERID}, #{QUANTITY})")
    boolean reservation(ReservationDTO reservationDTO); //예약 하기

//    @Select("select boardnum from board where USERID = #{USERID}")
//    ArrayList<Integer> userboard(int userid); //회원이 작성한 게시글(상품게시글)번호 가져오기

    @Select("select COALESCE(SUM(boardnum), 0) from reservation where BOARDNUM = #{BOARDNUM}")
    int reservationcheck(@Param("BOARDNUM")int BOARDNUM); //상품 수정 전 예약유무 체크

    @Select("SELECT r.*, b.boardsub, b.productprice FROM reservation r JOIN board b ON r.boardnum = b.boardnum WHERE r.HOSTUSERID = #{HOSTUSERID}")
    ArrayList<ReservationDTO> reservationlist(int HOSTUSERID); //예약정보를 불러오기

//    @Select("select * from reservation where HOSTUSERID = #{HOSTUSERID}")
//    ArrayList<ReservationDTO> reservationlist(int HOSTUSERID); //예약정보를 불러오기

    @Select("SELECT r.*, b.boardsub, b.productprice FROM reservation r JOIN board b ON r.boardnum = b.boardnum WHERE r.USERID = #{USERID}")
    ArrayList<ReservationDTO> myreservationlist(int USERID); //나의 거래 예약 정보 불러오기

    @Select("select boardsub from board WHERE boardnum = #{BOARDNUM}")
    String getboardsubject(int boardnum); //해당 게시글의 제목

    @Select("select nickname from users WHERE user_id = #{USER_ID}")
    String getnickname(int user_id);  //예약한 회원의 이름

    @Select("select productprice from board WHERE userid = #{userid} and boardnum = #{boardnum}")
    int getamount(@Param("userid")int userid, @Param("boardnum")int boardnum);  //거래할 상품 가격정보

//    @Update("UPDATE account SET amount = #{amount} WHERE userid = #{userid}")
//    void trans(@Param("amount") int amount, @Param("userid") int userid);   //회원 금액 변경

    @Update("UPDATE board SET productcount = #{productcount} WHERE boardnum = #{boardnum}")
    void changeproductcount(@Param("productcount") int productcount, @Param("boardnum") int boardnum);  //게시글(상품) 거래상태 변경

    @Update("UPDATE board SET productstatus = #{productstatus} WHERE boardnum = #{boardnum}")
    void changestatus(@Param("productstatus") String productstatus, @Param("boardnum") int boardnum);  //게시글(상품) 거래상태 변경

    @Delete("delete from reservation where reserveid = #{reserveid}")
    boolean deletereservation(@Param("reserveid")int reserveid);  //예약 정보 삭제

    @Select("select COALESCE(SUM(quantity), 0) from reservation WHERE boardnum = #{boardnum}")
    int checkreservation(int boardnum);  //예약 수량 확인

    @Select("select amount from account WHERE userid = #{userid}")
    int getuseramount(int userid);  //회원 보유금액
    

}
