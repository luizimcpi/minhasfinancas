package com.devlhse.minhasfinancas.config;

import com.devlhse.minhasfinancas.security.JWTAuthenticationFilter;
import com.devlhse.minhasfinancas.security.JWTAuthorizationFilter;
import com.devlhse.minhasfinancas.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.devlhse.minhasfinancas.security.constants.SecurityConstants.*;

@Configuration
public class WebSecurityConfig {

    private UserDetailsServiceImpl userDetailsService;
    private JwtConfig jwtConfig;

    public WebSecurityConfig(UserDetailsServiceImpl userService, JwtConfig jwtConfig) {
        this.userDetailsService = userService;
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(encoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(MAIN_URL, SIGN_UP_URL, SIGN_UP_URL_WITH_ROOT, USER_VERIFY_URL, USER_VERIFY_URL_WITH_ROOT).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(http), jwtConfig))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(http), jwtConfig))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new AuthFailureHandler());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addAllowedMethod(HttpMethod.POST);
        corsConfiguration.addAllowedMethod(HttpMethod.GET);
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        corsConfiguration.addAllowedMethod(HttpMethod.PATCH);
        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
        corsConfiguration.addAllowedOrigin("https://luizimcpi.github.io/financaspwa");
        source.registerCorsConfiguration("/**", corsConfiguration);


        return source;
    }


}
