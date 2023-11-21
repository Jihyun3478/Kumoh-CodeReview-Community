package com.kcr.controller;

import com.kcr.domain.dto.member.MemberRequestDTO;
import com.kcr.domain.dto.Response;
import com.kcr.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final AuthServiceImpl authServiceImpl;

    /* ================ API ================ */
    @PostMapping("/signup")
    public Response signUp(@RequestBody MemberRequestDTO requestDTO) {
        Response response = new Response();
        try {
            authServiceImpl.signUp(requestDTO);
            response.setResponse("success");
            response.setMessage("회원가입을 성공적으로 완료했습니다.");
        } catch (IllegalArgumentException e) {
            response.setResponse("failed");
            response.setMessage("회원가입을 하는 도중 오류가 발생했습니다.");
            response.setData(e.toString());
        }
        return response;
    }

    /* ================ UI ================ */

}
