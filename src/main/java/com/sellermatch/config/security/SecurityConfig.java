package com.sellermatch.config.security;

import com.sellermatch.util.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTUtil jwtUtil;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
                "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**","/swagger/**"
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // MD5 암호화 필요
        return new MessageDigestPasswordEncoder("MD5");
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // 활용 용도 확인
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable() // REST API만을 고려, 기본 설정 해제
            .csrf().disable() // csrf 사용 X
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션도 사용 X
            .and()
            .authorizeRequests()
            .antMatchers("/member/**").hasRole("USER")
            .anyRequest().permitAll()
            .and()
            .addFilterBefore(new SecurityJwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
            // JwtAuthenticationFilter는
            // UsernamePasswordAuthenticationFilter 전에 넣음
    }
}
