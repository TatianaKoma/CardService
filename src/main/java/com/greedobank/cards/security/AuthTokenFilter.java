package com.greedobank.cards.security;

import com.greedobank.cards.utils.JwtUtils;
import com.greedobank.cards.utils.ResponseMessages;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    private static final int UNAUTHORIZED_STATUS = 401;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if (token != null) {
            try {
                String email = JwtUtils.getEmail(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (SignatureException e) {
                setResponse(ResponseMessages.TOKEN_WRONG, response);
            } catch (ExpiredJwtException e) {
                setResponse(ResponseMessages.TOKEN_EXPIRED, response);
            } catch (UsernameNotFoundException | IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(ResponseMessages.AUTH_HEADER_NAME);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(ResponseMessages.AUTH_HEADER_PREFIX))
            return authHeader.substring(ResponseMessages.AUTH_HEADER_PREFIX.length());
        return null;
    }

    private void setResponse(String message, HttpServletResponse response) throws IOException {
        response.setStatus(UNAUTHORIZED_STATUS);
        response.getOutputStream().print(String.format(ResponseMessages.TEMPLATE_MESSAGE, message));
        response.getOutputStream().close();
    }
}
