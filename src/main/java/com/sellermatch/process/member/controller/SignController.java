package com.sellermatch.process.member.controller;

import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.util.EncryptionUtils;
import com.sellermatch.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class SignController {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @PostMapping("/signin")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public CommonDTO signin(@RequestBody Member member) {
        CommonDTO result = new CommonDTO();
        memberRepository.findByMemIdAndMemPw(member.getMemId(), EncryptionUtils.encryptMD5(member.getMemPw())).ifPresentOrElse(temp -> {
            Map<String, Object> jwt = jwtUtil.createToken(member.getMemId());
            result.setContent(jwt);
        }, () -> {
            Map<String, Object> jwt = new HashMap<>();
            jwt.put("token", "");
            jwt.put("expires", "");
            result.setContent(jwt);
            result.setResult("ERROR");
            result.setStatus(0);
        });
        return result;
    }

}
