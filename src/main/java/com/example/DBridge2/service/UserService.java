package com.example.DBridge2.service;

import com.example.DBridge2.DTO.ACCOUNTDTO;
import com.example.DBridge2.DTO.UserDTO;
import com.example.DBridge2.domain.Users;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface UserService {

    public String check_dupilcate(String username);

    public ResponseEntity<String> getuser_token(int userid,  HttpServletResponse response) throws IOException, InterruptedException;

    public ResponseEntity<String> getuseramount(String dbToken, HttpServletResponse response) throws IOException, InterruptedException;

    public boolean addaccount(ACCOUNTDTO accountdto, HttpServletResponse response) throws IOException, InterruptedException;

    public String getUser(String id, String pw);

    public void joinuser(Users users);
}
