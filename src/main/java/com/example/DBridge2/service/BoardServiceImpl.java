package com.example.DBridge2.service;

import com.example.DBridge2.DTO.*;
import com.example.DBridge2.mapper.BoardMapper;
import com.example.DBridge2.mapper.UserMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class BoardServiceImpl implements BoardService{

    // 서버 업로드 경로
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    // 최대 파일 크기
    private long MAX_SIZE = 10485760;

    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public int getBoardcnt() {  //총 게시물 개수
        return boardMapper.selectboardcnt();
    }

    @Override
    public ArrayList<BoardListDTO> getBoard(String numOfRows, String pageNo) { //게시판 리스트 불러오기  / 한 페이지당 나타낼 게시글 개수 / 페이지번호

        ArrayList<BoardListDTO> boardListDTOS = boardMapper.selectboard(((Integer.parseInt(pageNo)-1)*5), Integer.parseInt(numOfRows));

        for(int i = 0; i < boardListDTOS.size(); i++){

            //회원이름 불러오기
            boardListDTOS.get(i).setNICKNAME(userMapper.getnickname(Long.valueOf(boardMapper.selectboard(((Integer.parseInt(pageNo)-1)*5), Integer.parseInt(numOfRows)).get(i).getUSERID())));

            //이미지(파일) 경로 설정
            if(boardMapper.selectimg(boardListDTOS.get(i).getBOARDNUM()) != null){
                if(boardMapper.selectimgtype(boardListDTOS.get(i).getBOARDNUM()).equals("image/png")){
                    boardListDTOS.get(i).setFILEPATH(boardMapper.selectimg(boardListDTOS.get(i).getBOARDNUM()) + ".png");
                }else{
                    boardListDTOS.get(i).setFILEPATH(boardMapper.selectimg(boardListDTOS.get(i).getBOARDNUM()) + ".jpg");
                }
            }
        }

        return boardListDTOS;
    }

    @Override
    public ArrayList<BoardListDTO> getBoardInfo(int board_id) { //상품 상세정보 내용 가져오기
        ArrayList<BoardListDTO> boardListDTOS = boardMapper.selectboardinfo(board_id);

        String nickname = userMapper.getnickname(Long.valueOf(boardListDTOS.get(0).getUSERID()));
        boardListDTOS.get(0).setNICKNAME(nickname);

        return boardListDTOS;
    }

    @Override
    public ArrayList<FileDTO> getBoardImgInfo(int board_id) { //상품 상세정보 이미지(파일) 가져오기

        ArrayList<FileDTO> imgcnt = boardMapper.selectimginfo(board_id);

        for (int i = 0; i < imgcnt.size(); i++){
            if(imgcnt.get(i).getFILETYPE().equals("image/png")){
                imgcnt.get(i).setFILEPATH(imgcnt.get(i).getFILENAMEF() + ".png");
            }else{
                imgcnt.get(i).setFILEPATH(imgcnt.get(i).getFILENAMEF() + ".jpg");
            }
        }
        return imgcnt;
    }

    @Override
    public void addComment(COMMENTDTO commentdto) {

        boardMapper.addcomment(commentdto);
    }

    @Override
    public ArrayList<COMMENTDTO> getComment(int bnum) {

        return boardMapper.getcomment(bnum);
    }

    @Override
    public void deleteComment(int commentnum) {

        boardMapper.deletecomment(commentnum);
    }

    @Override
    public void addBoard(BoardDTO boardDTO) {   //상품등록

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        boardDTO.setBOARDUPTIME(formatter.format(date));
        boardDTO.setPRODUCTSTATUS("판매중");

        boardMapper.addboard(boardDTO);
    }

    @Override
    public void updateBoard(BoardDTO boardDTO) {    //상품 수정
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        boardDTO.setBOARDUPTIME(formatter.format(date));
        boardDTO.setPRODUCTSTATUS("판매중");

        boardMapper.updateBoard(boardDTO);
    }

    @Override
    public int getBoardNum(String userid) { //이미지 업로드 전 상품등록한 게시판 번호 가져오기 / 최근에 등록된 게시글(상품게시글) 번호
        return boardMapper.getboardnum(userid);
    }

    @Override
    public boolean getBoardDeleteImg(int filenum) throws IOException { //상품 수정페이지 > 이미지 삭제

        ArrayList<FileDTO> fileDTOS= boardMapper.selectdeleteimginfo(filenum); //몇번째 파일정보를 가져올것인지 / UUID / 삭제할 파일 정보 불러오기

        Path filePath = Paths.get(fileDTOS.get(0).getFILEPATH());  //DB에 저장된 파일의 경로를 불러와서 저장
        Files.delete(filePath); //해당 경로에 있는 파일을 삭제한다.

        return boardMapper.deleteimg(filenum);
    }

    @Override
    public boolean reservation(ReservationDTO reservationDTO) {  //예약 하기

        return boardMapper.reservation(reservationDTO);
    }

    @Override
    public boolean reservationcheck(int boardnum) {

        if(boardMapper.reservationcheck(boardnum) == 0){
            return false;
        }else{
            return true;
        }
    }

//    @Override
//    public ArrayList<Integer> userboard(int userid) { //회원이 작성한 게시글(상품게시글)번호 가져오기
//
//        return boardMapper.userboard(userid);
//    }

    @Override
    public ArrayList<ReservationDTO> reservationlist(int hostuserid) { //해당 게시글의 예약정보를 불러오기

        ArrayList<ReservationDTO> reservationDTOS = boardMapper.reservationlist(hostuserid);

        if(!reservationDTOS.isEmpty()){
            for(int i = 0; i < reservationDTOS.size(); i++){
                reservationDTOS.get(i).setNICKNAME(boardMapper.getnickname(reservationDTOS.get(i).getUSERID())); //예약한 회원의 이름
            }
        }

        return reservationDTOS;
    }

    @Override
    public ArrayList<ReservationDTO> myreservationlist(int userid) { //나의 거래 예약 정보 불러오기

        return boardMapper.myreservationlist(userid);
    }

    @Override
    public RETURNDTO guesttrans(ReservationDTO reservationDTO, HttpServletResponse response) throws IOException, InterruptedException {  //중고거래 진행 / 구매자

        reservationDTO.setAMOUNT((boardMapper.getamount(reservationDTO.getHOSTUSERID(), reservationDTO.getBOARDNUM())) * reservationDTO.getQUANTITY()); //거래할 상품 가격

        String guestusername = userMapper.getusername(reservationDTO.getUSERID());

        String AmountParam = URLEncoder.encode("AMOUNT", "UTF-8") + "=" + reservationDTO.getAMOUNT();
        String UsernameParam = URLEncoder.encode("USERNAME", "UTF-8") + "=" + URLEncoder.encode(guestusername, "UTF-8");
        String UserinfoParam = URLEncoder.encode("USERINFO", "UTF-8") + "=" + URLEncoder.encode("guest", "UTF-8");
        String StatusParam = URLEncoder.encode("STATUS", "UTF-8") + "=" + URLEncoder.encode("update", "UTF-8");

        String requestBody = AmountParam + "&" + UsernameParam + "&" + StatusParam + "&" + UserinfoParam;

        String authorizationUrl = "https://test-api.cyber-i.com/svc/jwpath/JW_DB_MAIN";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(authorizationUrl))
                .header("Authorization", "Bearer " + reservationDTO.getDBTOKEN()) // dbToken을 헤더에 추가
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> remoteResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        String responseBody = remoteResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        RETURNDTO returndto = new RETURNDTO();

        returndto.setMESSAGE(jsonNode.at("/response/0/MESSAGE").asText());
        returndto.setAMOUNT(Integer.parseInt(jsonNode.at("/response/0/AMOUNT").asText()));


        return returndto;
    }

    @Override
    public RETURNDTO hosttrans(ReservationDTO reservationDTO, HttpServletResponse response) throws IOException, InterruptedException {  //중고거래 진행 / 판매자

        reservationDTO.setAMOUNT((boardMapper.getamount(reservationDTO.getHOSTUSERID(), reservationDTO.getBOARDNUM())) * reservationDTO.getQUANTITY()); //거래할 상품 가격

        String hostusername = userMapper.getusername(reservationDTO.getHOSTUSERID());

        String AmountParam = URLEncoder.encode("AMOUNT", "UTF-8") + "=" + reservationDTO.getAMOUNT();
        String UsernameParam = URLEncoder.encode("USERNAME", "UTF-8") + "=" + URLEncoder.encode(hostusername, "UTF-8");
        String UserinfoParam = URLEncoder.encode("USERINFO", "UTF-8") + "=" + URLEncoder.encode("host", "UTF-8");
        String StatusParam = URLEncoder.encode("STATUS", "UTF-8") + "=" + URLEncoder.encode("update", "UTF-8");

        String requestBody = AmountParam + "&" + UsernameParam + "&" + StatusParam + "&" + UserinfoParam;

        String authorizationUrl = "https://test-api.cyber-i.com/svc/jwpath/JW_DB_MAIN";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(authorizationUrl))
                .header("Authorization", "Bearer " + reservationDTO.getDBTOKEN()) // dbToken을 헤더에 추가
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> remoteResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        String responseBody = remoteResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        RETURNDTO returndto = new RETURNDTO();

        returndto.setMESSAGE(jsonNode.at("/response/0/MESSAGE").asText());
        returndto.setAMOUNT(Integer.parseInt(jsonNode.at("/response/0/AMOUNT").asText()));

        return returndto;
    }

    @Override
    public int checkreservation(int boardnum) {  //예약 유무 체크

        return boardMapper.checkreservation(boardnum);
    }

    @Override
    public void deletereservation(ReservationDTO reservationDTO) {

        boardMapper.deletereservation(reservationDTO.getRESERVEID());

        ArrayList<BoardListDTO> boardinfo = boardMapper.selectboardinfo(reservationDTO.getBOARDNUM());

        //상품 수량 차감
        boardMapper.changeproductcount((boardinfo.get(0).getPRODUCTCOUNT() - reservationDTO.getQUANTITY()), reservationDTO.getBOARDNUM());

        //상품 수량이 0일경우 거래완료처리
        if((boardinfo.get(0).getPRODUCTCOUNT() - reservationDTO.getQUANTITY() == 0)){
            boardMapper.changestatus("거래완료", reservationDTO.getBOARDNUM());
        }
    }

    @Override
    public void refuse(int reserveid) { //예약 거절

        boardMapper.deletereservation(reserveid);
    }

    @Override
    public RETURNDTO deposit(ACCOUNTDTO accountdto) throws IOException, InterruptedException { //입금

        String authorizationUrl = "https://test-api.cyber-i.com/svc/jwpath/JW_DB_MAIN";

        // Request Body에 전송할 데이터 생성
        String requestBody = "AMOUNT=" + accountdto.getAMOUNT()
                + "&STATUS=" + URLEncoder.encode("deposit", "UTF-8");

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(authorizationUrl))
                .header("Authorization", "Bearer " + accountdto.getDBTOKEN()) // dbToken을 헤더에 추가
                .header("Content-Type", "application/x-www-form-urlencoded") // 요청 데이터의 타입을 지정
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // POST 메서드 및 Request Body 설정
                .build();

        HttpResponse<String> remoteResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        String responseBody = remoteResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        RETURNDTO returndto = new RETURNDTO();

        returndto.setMESSAGE(jsonNode.at("/response/0/MESSAGE").asText());
        returndto.setAMOUNT(Integer.parseInt(jsonNode.at("/response/0/AMOUNT").asText()));

        return returndto;
    }

    @Override
    public RETURNDTO withdrawal(ACCOUNTDTO accountdto) throws IOException, InterruptedException {

        String authorizationUrl = "https://test-api.cyber-i.com/svc/jwpath/JW_DB_MAIN";

        // Request Body에 전송할 데이터 생성
        String requestBody = "AMOUNT=" + accountdto.getAMOUNT()
                + "&STATUS=" + URLEncoder.encode("withdrawal", "UTF-8");

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(authorizationUrl))
                .header("Authorization", "Bearer " + accountdto.getDBTOKEN()) // dbToken을 헤더에 추가
                .header("Content-Type", "application/x-www-form-urlencoded") // 요청 데이터의 타입을 지정
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // POST 메서드 및 Request Body 설정
                .build();

        HttpResponse<String> remoteResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        String responseBody = remoteResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        RETURNDTO returndto = new RETURNDTO();

        returndto.setMESSAGE(jsonNode.at("/response/0/MESSAGE").asText());
        returndto.setAMOUNT(Integer.parseInt(jsonNode.at("/response/0/AMOUNT").asText()));

        return returndto;

    }

    @Override
    public HashMap save4(MultipartFile file, int boardnum) {    //이미지(파일) 업로드

        Path dir = Paths.get(uploadPath);

        if (!Files.exists(dir)) {
            try {
                // 경로가 없다면 생성
                Files.createDirectories(dir);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        HashMap result = new HashMap();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        // 파일 정보
        String fileName = file.getOriginalFilename();  //업로드되는 파일에서 확장자를 포함한 파일의 이름을 반환
        String fileSize = Long.toString(file.getSize()); //파일의 크기를 byte단위로 리턴
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1); //마침표가있는 마지막위치를 찾은 후 찾은 위치 뒤의 확장자를 찾는다
        String fileType = file.getContentType(); //파일의 타입 확인
        String filePath = "";
        // 결과 정보
        String status = "";
        String message = "";
        String fileSeq = "";

        // 1. 파일 사이즈
        if (file.getSize() > MAX_SIZE) {
            status = "fail";
            message = "file over max upload size";
            result.put("status", status);
            result.put("message", message);
            return result;
        }

        //파일이름 처음부터 .전까지 가져오기
        String tempName = fileName.substring(0, fileName.lastIndexOf("."));
        //properties에서 설정한 경로 + 파일구분자 + 파일이름 + . + 파일확장자
        filePath = dir.toString() + File.separator + tempName + formatter.format(date) +"." + fileExt;

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFILENAME(fileName);
        fileDTO.setFILENAMEF(tempName + formatter.format(date));
        fileDTO.setFILETYPE(fileType);
        fileDTO.setFILEPATH(filePath);
        fileDTO.setBOARDNUM(boardnum);

        try {
            InputStream is = file.getInputStream(); //파일의 내용을 1바이트 단위로 읽는 스트림
            Files.copy(is, dir.resolve(filePath), StandardCopyOption.REPLACE_EXISTING);
            //복사할대상 / 복사할 위치() / 기존에 같은 파일이 있으면 덮어쓴다.

            boardMapper.addfile(fileDTO);

            status = "success";
            message = "upload complete";

        } catch (Exception e) {
            e.printStackTrace();
            status = "fail";
            message = "upload fail";
        }
        result.put("status", status);
        result.put("message", message);

        return result;
    }
}
