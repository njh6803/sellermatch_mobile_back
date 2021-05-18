package com.sellermatch.process.member.controller;

import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.domain.Member;
import com.sellermatch.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SignController {

    private final JWTUtil jwtUtil;

    @PostMapping("/singin")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public CommonDTO signin(Member member) {
        CommonDTO result = new CommonDTO();
        String token = jwtUtil.createToken(member.getMemId());
        result.setContent(token);
        return result;
    }

}
