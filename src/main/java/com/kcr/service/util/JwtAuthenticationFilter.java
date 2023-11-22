//package com.kcr.service.util;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.kcr.domain.dto.member.login.LoginRequestDTO;
//import com.kcr.domain.dto.member.security.MyUserDetails;
//import com.kcr.domain.type.RoleType;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Slf4j(topic = "로그인 및 JWT 생성")
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private final JwtUtil jwtUtil;
//
//    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//        setFilterProcessesUrl("/api/user/login");
//    }
//
//    // 로그인 시, UsernamePasswordAuthenticationToken 을 발급
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
//            LoginRequestDTO requestDTO = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDTO.class);
//
//            return getAuthenticationManager().authenticate(
//                    new UsernamePasswordAuthenticationToken(requestDTO.getLoginId(), requestDTO.getLoginPw(),null)
//            );
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//    // 로그인 성공 시
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
//        String username = ((MyUserDetails) authResult.getPrincipal()).getUsername();
//        RoleType role = ((MyUserDetails) authResult.getPrincipal()).getMember().getRoleType();
//
//        String token = JwtUtil.createToken(username, role);
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
//    }
//
//    // 로그인 실패 시
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
//        response.setStatus(401);
//    }
//}