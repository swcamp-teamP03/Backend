package com.example.swcamp_p03.config.jwt;

import com.example.swcamp_p03.common.dto.ResponseLogin;
import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.user.dto.request.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.swcamp_p03.config.jwt.JwtProperties.HEADER_STRING;
import static com.example.swcamp_p03.config.jwt.JwtProperties.TOKEN_PREFIX;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도중");
        try{
            RequestLogin user = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            // UserDetailsService로 넘어감
            // authentication -> ProviderManager 구현체
            Authentication authentication = authenticationManager.authenticate(authRequest);
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authResult.getPrincipal();
        String jwtToken = jwtTokenUtils.generateJwtToken(userDetailsImpl);

        Cookie setCookie = new Cookie("accessToken", jwtToken);

        ObjectMapper objectMapper = new ObjectMapper();
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwtToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addCookie(setCookie);
        ResponseLogin loginSuccess = ResponseLogin.builder()
                .success(true)
                .message("Login Success")
                .token(jwtToken)
                .build();
        try {
            response.getWriter().write(objectMapper.writeValueAsString(loginSuccess));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        /*
         *	로그인을 한 상태에서 Token값을 주고받는 상황에서 잘못된 Token값이라면
         *	인증이 성공하지 못한 단계 이기 때문에 잘못된 Token값을 제거합니다.
         *	모든 인증받은 Context 값이 삭제 됩니다.
         */
        SecurityContextHolder.clearContext();
        super.unsuccessfulAuthentication(request, response, failed);
    }

}
