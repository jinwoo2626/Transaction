package com.example.DBridge2.service;

import com.example.DBridge2.DTO.ACCOUNTDTO;
import com.example.DBridge2.DTO.UserDTO;
import com.example.DBridge2.domain.Users;
import com.example.DBridge2.mapper.UserMapper;
import com.example.DBridge2.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String check_dupilcate(String username) {

        String msg= "";

        if(userMapper.check_duplicate(username) == null){
            System.out.println("userMapper.check_duplicate(username)1 = " + userMapper.check_duplicate(username));

            msg = "사용가능";
        }else{
            System.out.println("userMapper.check_duplicate(username)2 = " + userMapper.check_duplicate(username));
            msg = "사용불가능";
        }
        return msg;
    }

    //보유 금액 조회 / client_credentials > 토큰 발급
    @Override
    public ResponseEntity<String> getuser_token(int userid,  HttpServletResponse response) throws IOException, InterruptedException   {

        String authorizationUrl= "";

        if(userid == 2){
            authorizationUrl="https://test-api.cyber-i.com/svc/jwpath/Token_JW?client_id=root&client_secret=41262DC75D414039BC23BBED8648EF1417BFD0A1&grant_type=client_credentials";
        }else if(userid == 3){
            authorizationUrl="https://test-api.cyber-i.com/svc/jwpath/Token_JW?client_id=star22&client_secret=8A7913CBC26244789C7289F9894ADDAC182FA95B&grant_type=client_credentials";
        }else if(userid == 4){
            authorizationUrl="https://test-api.cyber-i.com/svc/jwpath/Token_JW?client_id=cheesecake26&client_secret=227620847E2042E796254E621BBFD3519D68EC48&grant_type=client_credentials";
        }else if(userid == 10){
            authorizationUrl="https://test-api.cyber-i.com/svc/jwpath/Token_JW?client_id=kiwi&client_secret=AAB1B760642E450B938AB44374D4DCAB742D9D1A&grant_type=client_credentials";
        } else if(userid == 13){
            authorizationUrl="https://test-api.cyber-i.com/svc/jwpath/Token_JW?client_id=jinwoo26&client_secret=DF36E26BE6F14DC19BAEB628CB262BC9E1C85E9C&grant_type=client_credentials";
        }

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(authorizationUrl))
                .GET()
                .build();

        HttpResponse<String> remoteResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return ResponseEntity.ok().body(remoteResponse.body());
    }

    @Override
    public ResponseEntity<String> getuseramount(String dbToken, HttpServletResponse response) throws IOException, InterruptedException {

        String StatusParam = URLEncoder.encode("STATUS", "UTF-8") + "=" + URLEncoder.encode("select", "UTF-8");

        String authorizationUrl = "https://test-api.cyber-i.com/svc/jwpath/JW_DB_MAIN?" + StatusParam;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(authorizationUrl))
                .header("Authorization", "Bearer " + dbToken) // dbToken을 헤더에 추가
                .GET()
                .build();

        HttpResponse<String> remoteResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // 반환된 JSON 형식의 값을 ResponseEntity로 반환
        return ResponseEntity.ok().body(remoteResponse.body());
    }

    @Override
    public boolean addaccount(ACCOUNTDTO accountdto, HttpServletResponse response) throws IOException, InterruptedException {

        String AccountNumParam = URLEncoder.encode("ACCOUNTNUM", "UTF-8") + "=" + URLEncoder.encode(accountdto.getACCOUNTNUM(), "UTF-8");
        String BankInfoParam = URLEncoder.encode("BANKINFO", "UTF-8") + "=" + URLEncoder.encode(accountdto.getBANKINFO(), "UTF-8");
        String AmountParam = URLEncoder.encode("AMOUNT", "UTF-8") + "=" + accountdto.getAMOUNT();
        String StatusParam = URLEncoder.encode("STATUS", "UTF-8") + "=" + URLEncoder.encode("insert", "UTF-8");


        String authorizationUrl = "https://test-api.cyber-i.com/svc/jwpath/JW_DB_MAIN?"
                + AccountNumParam + "&" + BankInfoParam + "&" + AmountParam + "&" + StatusParam;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(authorizationUrl))
                .header("Authorization", "Bearer " + accountdto.getDBTOKEN()) // dbToken을 헤더에 추가
                .GET()
                .build();

        HttpResponse<String> remoteResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // 반환된 JSON 형식의 값을 ResponseEntity로 반환
        ResponseEntity.ok().body(remoteResponse.body());

        System.out.println("ResponseEntity.ok().body(remoteResponse.body() = " + ResponseEntity.ok().body(remoteResponse.body()));

        return true;
    }

    @Override
    public String getUser(String id, String pw) { //!pw.equals(userMapper.getpw(id)

        if(pw.equals(userMapper.getpw(id))){ //로그인성공
            return "success";
        }else if (userMapper.getpw(id) == null){ //미회원인경우
            return "nomember";
        }else{  //비밀번호가 다른경우
            return "differentpw";
        }
    }

    @Override
    public void joinuser(Users users) {

        String rawPassword = users.getPassword();   //암호화 X 비밀번호
        String encPassword = bCryptPasswordEncoder.encode(rawPassword); //비밀번호 암호화
        users.setPassword(encPassword);

        userRepository.save(users); //axios로 받은 데이터에 이메일, 유저역할을 추가한후 jpa로 데이터를 save(insert)한다.

    }
}
