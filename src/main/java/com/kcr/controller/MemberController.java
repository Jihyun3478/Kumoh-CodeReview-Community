package com.kcr.controller;

import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.member.Response;
import com.kcr.domain.dto.member.login.LoginRequestDTO;
import com.kcr.domain.dto.member.mail.MailRequestDTO;
import com.kcr.domain.dto.member.signup.SignupRequestDTO;
import com.kcr.domain.entity.Member;
import com.kcr.service.AuthServiceImpl;
import com.kcr.service.util.CookieUtil;
import com.kcr.service.util.JwtUtil;
import com.kcr.service.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final AuthServiceImpl authServiceImpl;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    /* ================ API ================ */
    /* 회원가입 */
//    @PostMapping("/signup")
//    public ResponseEntity<MsgResponseDTO> signup(@RequestBody @Valid SignupRequestDTO signupRequestDTO) {
//        authServiceImpl.signup(signupRequestDTO);
//        return ResponseEntity.ok(new MsgResponseDTO("회원가입 완료", HttpStatus.OK.value()));
//    }

    /* 로그인 */
//    @PostMapping("/login")
//    public ResponseEntity<MsgResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto, HttpServletResponse response) {
//        authServiceImpl.login(loginRequestDto, response);
//        return ResponseEntity.ok(new MsgResponseDTO("로그인 완료", HttpStatus.OK.value()));
//    }

    @PostMapping("/api/signup")
    public com.kcr.domain.dto.member.Response signUp(@RequestBody Member member) {
        Response response = new Response();
        try {
            authServiceImpl.signUp(member);
            response.setResponse("success");
            response.setMessage("회원가입을 성공적으로 완료했습니다.");
        } catch (IllegalArgumentException e) {
            response.setResponse("failed");
            response.setMessage("회원가입을 하는 도중 오류가 발생했습니다.");
            response.setData(e.toString());
        }
        return response;
    }

//    @PostMapping("/api/login")
//    public com.kcr.domain.dto.member.Response signIn(@RequestBody MemberRequestDTO requestDTO) {
//        try {
//            authServiceImpl.signIn(requestDTO.getLoginId(), requestDTO.getLoginPw());
//            return new Response("success", "로그인을 성공적으로 완료했습니다.", null);
//        } catch (Exception e) {
//            return new Response("error", "로그인을 하는 도중 오류가 발생했습니다.", null);
//        }
//    }

    @PostMapping("/api/login")
    public Response login(@RequestBody LoginRequestDTO loginDTO, HttpServletRequest req, HttpServletResponse res) {
        try {
            final Member member = authServiceImpl.signIn(loginDTO.getLoginId(), loginDTO.getLoginPw());
            final String token = jwtUtil.generateToken(member);
            final String refreshJwt = jwtUtil.generateRefreshToken(member);
            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);
            redisUtil.setDataExpire(refreshJwt, member.getLoginId(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            res.addCookie(accessToken);
            res.addCookie(refreshToken);
            return new Response("success", "로그인에 성공했습니다.", token);
        } catch (Exception e) {
            return new Response("error", "로그인에 실패했습니다.", e.getMessage());
        }
    }

    @PostMapping("/api/signup/verify")
    public Response verify(@RequestBody MailRequestDTO emailDTO, HttpServletRequest req, HttpServletResponse res) {
        Response response;
        try {
            Member member = authServiceImpl.findByLoginId(emailDTO.getLoginId());
            authServiceImpl.sendVerificationMail(member);
            response = new Response("success", "성공적으로 인증메일을 보냈습니다.", null);
        } catch (Exception exception) {
            response = new Response("error", "인증메일을 보내는데 문제가 발생했습니다.", exception);
        }
        return response;
    }

    /* ================ UI ================ */
    @GetMapping("/signup/verify/{key}")
    public Response getVerify(@PathVariable String key) {
        Response response;
        try {
            authServiceImpl.verifyEmail(key);
            response = new Response("success", "성공적으로 인증메일을 확인했습니다.", null);

        } catch (Exception e) {
            response = new Response("error", "인증메일을 확인하는데 실패했습니다.", null);
        }
        return response;
    }
}
