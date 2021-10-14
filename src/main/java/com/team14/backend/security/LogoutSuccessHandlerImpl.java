package com.team14.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team14.backend.dto.ResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ObjectMapper om = new ObjectMapper();

        //성공 메시지
        ResponseDto responseDto = new ResponseDto("success", "성공적으로 로그아웃이 되었습니다.", "");

        //JSON 형식으로 변환
        String jsonString = om.writeValueAsString(responseDto);

        //JSON 형식, 한글 처리
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        //출력
        out.print(jsonString);
        out.flush();
        out.close();
    }
}
