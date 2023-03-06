package com.example.swcamp_p03.config.jwt;

import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.user.entity.User;
import com.example.swcamp_p03.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.swcamp_p03.config.jwt.JwtProperties.HEADER_STRING;
import static com.example.swcamp_p03.config.jwt.JwtProperties.TOKEN_PREFIX;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public JwtAuthorizationFilter(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtTokenUtils jwtTokenUtils) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        log.info("인증이나 권한이 필요한 주소 요청이 됨");
        String jwtHeader = getHeader(request, response, chain);
        if (jwtHeader == null) {
            return;
        }

        String jwtToken = getJwtToken(jwtHeader);
        String username = jwtTokenUtils.extractUserId(jwtToken);

        checkUser(username);
        System.out.println("인증성공 후 token : " + jwtToken);
        chain.doFilter(request, response);
    }

    private static String getHeader(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader(HEADER_STRING);
        if (jwtHeader == null || !jwtHeader.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return null;
        }
        return jwtHeader;
    }

    private static String getJwtToken(String jwtHeader) {
        return jwtHeader.replace(TOKEN_PREFIX, "");
    }

    private void checkUser(String username) {
        if (username != null) {
            User user = userRepository.findByUsername(username).orElse(null);
            UserDetails userDetails = new UserDetailsImpl(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
