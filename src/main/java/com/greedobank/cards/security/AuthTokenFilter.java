package com.greedobank.cards.security;

import com.greedobank.cards.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
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

import static com.greedobank.cards.utils.ResponseMessages.AUTH_HEADER_NAME;
import static com.greedobank.cards.utils.ResponseMessages.AUTH_HEADER_PREFIX;
import static com.greedobank.cards.utils.ResponseMessages.TEMPLATE_MESSAGE;
import static com.greedobank.cards.utils.ResponseMessages.TOKEN_EXPIRED;
import static com.greedobank.cards.utils.ResponseMessages.TOKEN_WRONG;

@Slf4j
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
                setResponse(TOKEN_WRONG.getDescription(), response);
            } catch (ExpiredJwtException e) {
                setResponse(TOKEN_EXPIRED.getDescription(), response);
            } catch (UsernameNotFoundException | IllegalArgumentException e) {
                log.error(e.getMessage(), e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER_NAME.getDescription());

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(AUTH_HEADER_PREFIX.getDescription()))
            return authHeader.substring(AUTH_HEADER_PREFIX.getDescription().length());
        return null;
    }

    private void setResponse(String message, HttpServletResponse response) throws IOException {
        response.setStatus(UNAUTHORIZED_STATUS);
        response.getOutputStream().print(String.format(TEMPLATE_MESSAGE.getDescription(), message));
        response.getOutputStream().close();
    }
}
