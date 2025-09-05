package com.ssafy.a705.global.security.config;

import com.ssafy.a705.global.common.redis.RedisService;
import com.ssafy.a705.global.security.handler.CustomAccessDeniedHandler;
import com.ssafy.a705.global.security.handler.CustomAuthenticationEntryPoint;
import com.ssafy.a705.global.security.jwt.filter.JsonUsernamePasswordAuthenticationFilter;
import com.ssafy.a705.global.security.jwt.filter.JwtAuthenticationProcessingFilter;
import com.ssafy.a705.global.security.jwt.service.JwtProvider;
import com.ssafy.a705.global.security.login.handler.LoginFailureHandler;
import com.ssafy.a705.global.security.login.handler.LoginSuccessHandler;
import com.ssafy.a705.global.security.login.service.CustomUserDetailsService;
import com.ssafy.a705.global.security.logout.handler.JwtLogoutHandler;
import com.ssafy.a705.global.security.logout.service.BlacklistService;
import com.ssafy.a705.global.security.oauth2.handler.OAuth2LoginFailureHandler;
import com.ssafy.a705.global.security.oauth2.handler.OAuth2LoginSuccessHandler;
import com.ssafy.a705.global.security.oauth2.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private static final String ERROR_URL = "/error";
    private static final String WEB_SOCKET_URL = "/ws/**";
    private static final String LOGIN_URL = "/api/v1/auth/login";
    private static final String LOCATION_DB_URL = "/api/v1/locations/infos";
    private static final String[] AUTH_URLS = {
            "/api/v1/auth/signup", "/api/v1/auth/verify", "/api/v1/auth/verify/resend",
            "/api/v1/auth/email"
    };
    private static final String[] SWAGGER_URLS = {
            "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**"
    };
    private static final String[] DOMAIN_URLS = {
            "/api/v1/boards", "/api/v1/locations", "/api/v1/groups"
    };
    private static final String KAKAO_TOKEN_URL = "/api/v1/auth/kakao/**";
    private static final String LOGOUT_URL = "/api/v1/auth/logout";
    private static final String ASSET_LINKS_URL = "/.well-known/assetlinks.json";


    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    private final JwtLogoutHandler jwtLogoutHandler;

    private final BlacklistService blacklistService;
    private final LogoutSuccessHandler logoutSuccessHandler =
            (req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK);


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
            PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
                AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            AuthenticationManager authenticationManager)
            throws Exception {
        JsonUsernamePasswordAuthenticationFilter jsonAuthenticationFilter = new JsonUsernamePasswordAuthenticationFilter(
                LOGIN_URL, authenticationManager, loginSuccessHandler, loginFailureHandler);
        JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter = new JwtAuthenticationProcessingFilter(
                jwtProvider, redisService, userDetailsService, blacklistService);
        http.getSharedObject(
                        AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        config -> config.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ASSET_LINKS_URL).permitAll()
                        .requestMatchers(WEB_SOCKET_URL).permitAll()
                        .requestMatchers(AUTH_URLS).permitAll()
                        .requestMatchers(KAKAO_TOKEN_URL).permitAll()
                        .requestMatchers(LOGIN_URL).permitAll()
                        .requestMatchers(DOMAIN_URLS).permitAll()
                        .requestMatchers(ERROR_URL).permitAll()
                        .requestMatchers(SWAGGER_URLS).permitAll()
                        .requestMatchers(LOCATION_DB_URL).hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                                .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .addFilterAt(jsonAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationProcessingFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl(LOGOUT_URL)                 // POST /v1/auth/logout
                        .addLogoutHandler(jwtLogoutHandler)
                        .logoutSuccessHandler(logoutSuccessHandler)
                );

        http.oauth2Login(oauth2Login ->
                oauth2Login
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint(
                                userInfo -> userInfo.userService(customOAuth2UserService)));
        return http.build();
    }
}
