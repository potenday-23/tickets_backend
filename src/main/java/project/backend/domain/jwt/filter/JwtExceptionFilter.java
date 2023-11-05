package project.backend.domain.jwt.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import project.backend.global.error.ErrorResponse;
import project.backend.global.error.exception.ErrorCode;

import javax.security.sasl.AuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            //토큰의 유효기간 만료
            setErrorResponse(request, response, ErrorCode.TOKEN_NOT_VALID);
        } catch (JwtException e) {
            //유효하지 않은 토큰
            setErrorResponse(request, response, ErrorCode.TOKEN_NOT_VALID);
        }
//        catch (IllegalStateException e) {
//            setErrorResponse(request, response, ExceptionCode.LOGOUT_MEMBER);
         catch (AuthenticationException e) {
            setErrorResponse(request, response, ErrorCode.AUTHORIZATION_HEADER_NOT_VALID);
        }
    }

    private void setErrorResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorCode errorCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();

        int status = 400;
        String error = HttpStatus.valueOf(status).getReasonPhrase();
        String exception = errorCode.getClass().getName();
        String message = errorCode.getMessage();
        String path = request.getServletPath();

        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}