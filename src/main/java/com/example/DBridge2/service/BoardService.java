package com.example.DBridge2.service;

import com.example.DBridge2.DTO.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public interface BoardService {

    public int getBoardcnt();

    public ArrayList<BoardListDTO> getBoard(String numOfRows, String pageNo);

    public ArrayList<BoardListDTO> getBoardInfo(int board_id);

    public ArrayList<FileDTO> getBoardImgInfo(int board_id);

    public void addComment(COMMENTDTO commentdto);

    public ArrayList<COMMENTDTO> getComment(int bnum);

    public void deleteComment(int commentnum);

    public void addBoard(BoardDTO boardDTO);

    public void updateBoard(BoardDTO boardDTO);

    public int getBoardNum(String userid);

    public boolean getBoardDeleteImg(int filenum) throws IOException;

    public boolean reservation(ReservationDTO reservationDTO);

//    public ArrayList<Integer> userboard(int userid);

    public boolean reservationcheck(int boardnum);

    public ArrayList<ReservationDTO> reservationlist(int boardnum);

    public ArrayList<ReservationDTO> myreservationlist(int userid);

    public RETURNDTO guesttrans(ReservationDTO reservationDTO, HttpServletResponse response) throws IOException, InterruptedException ;

    public RETURNDTO hosttrans(ReservationDTO reservationDTO, HttpServletResponse response) throws IOException, InterruptedException ;

    public int checkreservation(int boardnum);

    public void deletereservation(ReservationDTO reservationDTO);

    public void refuse(int reserveid);

    public RETURNDTO deposit(ACCOUNTDTO accountdto) throws IOException, InterruptedException;

    public RETURNDTO withdrawal(ACCOUNTDTO accountdto) throws IOException, InterruptedException;

    HashMap save4(MultipartFile files, int board_id);

}
