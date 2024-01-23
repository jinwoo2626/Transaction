package com.example.DBridge2.config.oauth2;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;

public class Authorization {

    public static void main() {
        String client_id = "jwuser";
        String redirect_uri = "/WEB-APP/oauth/auth_code.jsp";
        int userid = 2;
//        String authorizationEndpoint = "https://oauth2/authorize"; // 인증 엔드포인트 URL

        WebClient webClient = WebClient.builder().build();

        // 권한 부여 코드 요청을 위한 URL 생성
//        String authorizationUrl = "https://test-api.cyber-i.com/svc/sample/test/authorize"+
//                "?response_type=code" +
//                "&client_id=" + client_id +
//                "&redirect_uri=" + redirect_uri;

        String authorizationUrl = "https://test-api.cyber-i.com/svc/jwpath/JW_DB?userid=2";
//                "?response_type=code" +
//                "&client_id=" + client_id +
//                "&redirect_uri=" + redirect_uri;

        // WebClient를 사용하여 권한 부여 코드 요청
        String response = webClient.get()
                .uri(authorizationUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Response: " + response);
    }

    public static void apitest(){
//        WebClient webClient = WebClient.builder().build();
//        String authorizationUrl = "https://test-api.cyber-i.com/svc/sample/test/authorize?client_id=jwuser&redirect_uri=https://test-api.cyber-i.com/svc/hsbtest/hsbtest1";
//
//
//        // WebClient를 사용하여 권한 부여 코드 요청
//        String response = webClient.get()
//                .uri(authorizationUrl)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        response.sendRedirect(response);
//
//        return null;
    }
}
