package project.backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.backend.domain.jwt.filter.JwtExceptionFilter;
import project.backend.domain.jwt.filter.JwtFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {


    @Value("${jwt.secret}")
    private String secretKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()

                // enable h2-console
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다.
                .requestMatchers("/api/auth/kakao/login").permitAll() // 로그인 api
                .requestMatchers("/v3/api-docs").permitAll() // 회원가입 api
                .requestMatchers("/swagger*/**").permitAll() // 회원가입 api
                .requestMatchers("/swagger-ui").permitAll() // 회원가입 api
                .requestMatchers("/swagger-resources/**").permitAll() // 회원가입 api
                .requestMatchers("/api/tickets").permitAll() // 회원가입 api
                .requestMatchers("/api/tickets/**").permitAll() // 회원가입 api
                .requestMatchers("/api/categorys").permitAll() // 회원가입 api
                .requestMatchers("/favicon.ico").permitAll()
                //.anyRequest().authenticated() // 그 외 인증 없이 접근X

                .and()
                .addFilterBefore(new JwtFilter(secretKey), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtFilter.class)
                .getOrBuild()
                ;
    }



}