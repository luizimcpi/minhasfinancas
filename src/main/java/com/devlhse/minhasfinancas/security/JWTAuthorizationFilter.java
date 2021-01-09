package com.devlhse.minhasfinancas.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.devlhse.minhasfinancas.config.JwtConfig;
import com.devlhse.minhasfinancas.exception.AutenticacaoException;
import com.devlhse.minhasfinancas.exception.AutorizacaoException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.devlhse.minhasfinancas.security.constants.SecurityConstants.HEADER_STRING;
import static com.devlhse.minhasfinancas.security.constants.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtConfig jwtConfig;

    public JWTAuthorizationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
        super(authManager);
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    // Reads the JWT from the Authorization header, and then uses JWT to validate the token
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws IOException {
        String token = request.getHeader(HEADER_STRING);
        String usuarioIdHeader = request.getHeader("usuarioId");

        if (token != null && usuarioIdHeader != null) {
            // parse the token.
            String loggedUser = JWT.require(Algorithm.HMAC512(jwtConfig.getJwtSecret().getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();

            if (loggedUser != null) {
                if(!loggedUser.equals(usuarioIdHeader)){
                    throw new AutorizacaoException("Send valid user id in header.");
                }
                // new arraylist means authorities
                return new UsernamePasswordAuthenticationToken(loggedUser, null, new ArrayList<>());
            }

            return null;
        }

        throw new AutenticacaoException("Send JWT Token and user id in header.");
    }
}
