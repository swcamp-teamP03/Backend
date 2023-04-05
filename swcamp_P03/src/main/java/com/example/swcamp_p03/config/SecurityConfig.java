package com.example.swcamp_p03.config;

import com.example.swcamp_p03.config.jwt.JwtAuthenticationFilter;
import com.example.swcamp_p03.config.jwt.JwtAuthorizationFilter;
import com.example.swcamp_p03.config.jwt.JwtTokenUtils;
import com.example.swcamp_p03.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final CharacterEncodingFilter filter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(
                http.getSharedObject(AuthenticationConfiguration.class));
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
                authenticationManager, jwtTokenUtils);
        jwtAuthenticationFilter.setUsernameParameter("email");

        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(corsFilter())
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class)
                .addFilter(jwtAuthenticationFilter)
                .addFilter(
                        new JwtAuthorizationFilter(authenticationManager, userRepository, jwtTokenUtils))
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        http
                .authorizeRequests()
                .antMatchers("/sign/*").permitAll()
                .antMatchers("/redirect/*").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        config.addAllowedOrigin("http://localhost:3090");
        config.addAllowedOrigin("http://copyt-sw.s3-website.ap-northeast-2.amazonaws.com");
        config.addAllowedOrigin("http://copyt.co.kr.s3-website.ap-northeast-2.amazonaws.com");
        config.addAllowedOrigin("http://www.copyt.co.kr/");
        config.addExposedHeader("*");
        config.addAllowedHeader("*"); // 모든 header에 응답을 허용하겠다.
        config.addAllowedMethod("*"); // 모든 api 메소드 요청을 허용하겠다.
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
