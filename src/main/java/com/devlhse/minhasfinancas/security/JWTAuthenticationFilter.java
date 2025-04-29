package com.devlhse.minhasfinancas.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.devlhse.minhasfinancas.api.dto.UsuarioDTO;
import com.devlhse.minhasfinancas.api.dto.UsuarioLoginDTO;
import com.devlhse.minhasfinancas.config.JwtConfig;
import com.devlhse.minhasfinancas.model.entity.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.devlhse.minhasfinancas.security.constants.SecurityConstants.EXPIRATION_TIME;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JwtConfig jwtConfig;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;

        setFilterProcessesUrl("/api/usuarios/autenticar");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res){
        try {
            UsuarioLoginDTO creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UsuarioLoginDTO.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getSenha(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = JWT.create()
                .withSubject(((CustomUserDetails) auth.getPrincipal()).getUsuario().getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(jwtConfig.getJwtSecret().getBytes()));

        String id = ((CustomUserDetails) auth.getPrincipal()).getUsuario().getId().toString();
        String nome = ((CustomUserDetails) auth.getPrincipal()).getUsuario().getNome();
        String email = ((CustomUserDetails) auth.getPrincipal()).getUsuario().getEmail();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userTokenResponse = mapper.createObjectNode();
        userTokenResponse.put("id", id);
        userTokenResponse.put("nome", nome);
        userTokenResponse.put("email", email);
        userTokenResponse.put("accessToken", token);

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userTokenResponse);

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(json);
        res.getWriter().flush();
    }
}
