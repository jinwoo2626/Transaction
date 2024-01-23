package com.example.DBridge2.controller;

import com.example.DBridge2.DTO.*;
import com.example.DBridge2.service.BoardService;
import com.example.DBridge2.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@RestController
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @GetMapping("/")
    public void main(Model model){

    }

    @GetMapping("/main")
    public String main1(Model model){

        return "main";
    }

    @GetMapping("/userinfo")
    public String userinfo(Model model) {

        return "userinfo";
    }

    @GetMapping("/transaction")
    public String transaction(Model model) {

        return "transaction";
    }

    @GetMapping("/login") //SpringSecurity가 해당주소를 낚아챈다 > 로그인화면으로 / SecurityConfig파일 생성 후 작동 X
    public String login(Model model) {

        return "login";
    }

    @GetMapping("/join")
    public String join(Model model) {

        return "join";
    }

    @GetMapping("/logout")
    public String logout(Model model) {

        return "logout";
    }

    @GetMapping("/addboard")
    public String addboard(Model model) {

        return "addboard";
    }

    @GetMapping("/check_duplicate")
    public String check_duplicate(Model model, @RequestParam("username") String username) {

        return userService.check_dupilcate(username);
    }

    @PostMapping("/addproduct") //상품등록
    public void addproduct(@RequestBody BoardDTO boardDTO){

        boardService.addBoard(boardDTO);
    }

    @PostMapping(value = "/addproductimg", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})    //이미지 업로드
    public boolean addproductimg(@RequestPart("productimage") MultipartFile[] files, @RequestParam("userid") String userid){

        for (MultipartFile file : files){
            if (!file.getOriginalFilename().isEmpty()) {                //업로드 된 파일이 존재할 때
                boardService.save4(file, boardService.getBoardNum(userid));  //파일 업로드
            }
        }
        return false;
    }

    @GetMapping("/boardcnt")  //총 게시물 개수
    public int board(Model model){

        return boardService.getBoardcnt();
    }

    @GetMapping("/board")    //게시판 리스트 불러오기 /@RequestParam -> 한 페이지당 나타낼 게시글 개수 / 페이지번호
    public ArrayList<BoardListDTO> board(@RequestParam("numOfRows") String numOfRows, @RequestParam("pageNo") String pageNo){

        BoardDTO boardDTO = new BoardDTO();
        ArrayList<BoardListDTO> board = boardService.getBoard(numOfRows, pageNo);

        return board;
    }
    @GetMapping("/boarddetail") //상품 상세정보 이동
    public String boarddetail(Model model){

        return "boarddetail";
    }
    @GetMapping("/boardinfo")   //상품 상세정보 내용 가져오기
    public ArrayList<BoardListDTO> boardinfo(@RequestParam("bnum") int bnum){
        return boardService.getBoardInfo(bnum);
    }

    @GetMapping("/boardimginfo")  //상품 상세정보 이미지(파일) 가져오기
    public ArrayList<FileDTO> boardimginfo(@RequestParam("bnum") int bnum){
        return boardService.getBoardImgInfo(bnum);
    }

    @PostMapping("/addcomment")  //댓글달기
    public void addcomment(@RequestBody COMMENTDTO commentdto) throws IOException {

        boardService.addComment(commentdto);
    }

    @GetMapping("/getcomment")  //댓글 조회
    public ArrayList<COMMENTDTO> getcomment(@RequestParam("bnum") int bnum){

        return boardService.getComment(bnum);
    }
    @PostMapping("/deletecomment")  //댓글삭제
    public void deletecomment(@RequestBody COMMENTDTO commentdto) throws IOException {

        boardService.deleteComment(commentdto.getCOMMENTNUM());
    }

    @PostMapping("/deleteimg")  //상품 수정페이지 > 이미지 삭제
    public boolean deleteimg(@RequestBody FileDTO fileDTO) throws IOException {

        return boardService.getBoardDeleteImg(fileDTO.getFILENUM());
    }

    @GetMapping("/boardupdate") //상품 수정페이지 이동
    public String boardupdate(Model model){

        return "boardupdate";
    }
    @PostMapping("/updateproduct")  //상품 수정
    public void updateproduct(@RequestBody BoardDTO boardDTO){

        boardService.updateBoard(boardDTO);
    }

    @PostMapping(value = "/updateproductimg", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}) //상품 수정페이지 이미지 업로드
    public boolean updateproductimg(@RequestPart("productimage") MultipartFile[] files, @RequestParam("boardnum") int boardnum){

        for (MultipartFile file : files){
            if (!file.getOriginalFilename().isEmpty()) {                //업로드 된 파일이 존재할 때
                boardService.save4(file, boardnum);  //파일 업로드
            }
        }
        return false;
    }
//    @GetMapping("/userboard")   //상품 예약확인 / 회원이 작성한 게시글(상품게시글)번호 가져오기
//    public ArrayList<Integer> userboard(@RequestParam("userid") int userid){
//
//        return boardService.userboard(userid);
//    }

    @GetMapping("/reservationcheck")  //상품 수정 시 예약유무 확인
    public boolean reservationcheck(@RequestParam("bnum") int bnum){

        return boardService.reservationcheck(bnum);
    }

    @GetMapping("/reservationlist") //해당 게시글의 예약정보를 불러오기
    public ArrayList<ReservationDTO> reservationlist(@RequestParam("hostuserid") int hostuserid){

        return boardService.reservationlist(hostuserid);
    }
    @GetMapping("/myreservationlist")  //나의 거래 예약 정보 불러오기
    public ArrayList<ReservationDTO> myreservationlist(@RequestParam("userid") int userid){

        return boardService.myreservationlist(userid);
    }

    @GetMapping("/checkreservation")  //예약 유무 확인
    public int checkreservation(@RequestParam("bnum") int bnum){

        return boardService.checkreservation(bnum);
    }

    @PostMapping("/reservation")    //예약 하기
    public boolean reservation(@RequestBody ReservationDTO reservationDTO){

        return boardService.reservation(reservationDTO);
    }

    @PostMapping("/trans")  //중고거래
    public RETURNDTO trans(@RequestBody ReservationDTO reservationDTO , HttpServletResponse response) throws IOException, InterruptedException {

        RETURNDTO returndto = new RETURNDTO();

        String guestmessage = (boardService.guesttrans(reservationDTO, response)).getMESSAGE();    //구매자 거래

        if(!guestmessage.equals("구매자의 잔액이 부족합니다.")){
            returndto = boardService.hosttrans(reservationDTO, response);  //판매자 거래

            String hostmessage = returndto.getMESSAGE();

            if(guestmessage.equals("SUCCESS") && (hostmessage.equals("SUCCESS"))) {
                boardService.deletereservation(reservationDTO);     //상품 수량 차감
                returndto.setMESSAGE("거래가 완료되었습니다.");
            }else{
                returndto.setMESSAGE("거래가 미완료되었습니다.");
            }
        }else{
            returndto.setMESSAGE("구매자의 잔액이 부족합니다.");
        }
        return returndto;
    }

    @GetMapping("/getuser_token") // client_credentials > 토큰 발급
    public ResponseEntity<String> getuser_token(@RequestParam("userid") int userid, HttpServletResponse response) throws IOException, InterruptedException {

        return userService.getuser_token(userid, response);
    }

    @GetMapping("/getuseramount")  //보유 금액 조회 / 토큰인증
    public ResponseEntity<String> getuseramount(@RequestParam("dbToken") String dbToken, HttpServletResponse response) throws IOException, InterruptedException {

        return userService.getuseramount(dbToken, response);
    }

    @GetMapping("/addaccount")
    public String addaccount(Model model) {

        return "addaccount";
    }

    @PostMapping("/addaccount")    //계좌 등록
    public boolean addaccount(@RequestBody ACCOUNTDTO accountdto, HttpServletResponse response) throws IOException, InterruptedException{

        return userService.addaccount(accountdto, response);
    }

    @PostMapping("/refuse")    //예약 거절
    public void refuse(@RequestBody ReservationDTO reservationDTO){

        boardService.refuse(reservationDTO.getRESERVEID());
    }

    @PostMapping("/deposit")    //입금
    public RETURNDTO deposit(@RequestBody ACCOUNTDTO accountdto) throws IOException, InterruptedException {

        return boardService.deposit(accountdto);
    }

    @PostMapping("/withdrawal")    //출금
    public RETURNDTO withdrawal(@RequestBody ACCOUNTDTO accountdto) throws IOException, InterruptedException {

        return boardService.withdrawal(accountdto);
    }

}
