package com.sparta.myselectshop.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.myselectshop.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class kakaoController {
    private final KakaoService kakaoService;

    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {
        String token = kakaoService.kakaoLogin(code); //jwt
        //token에 한글 코드가 있어 오류 그래서 인코딩해줌
        String encode = URLEncoder.encode(token, "utf-8");

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, encode);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }
}
