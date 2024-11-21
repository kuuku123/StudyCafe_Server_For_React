package com.StudyCafe_R.infra.config;

import com.StudyCafe_R.infra.security.CustomAuthorizationRequestResolver;
import com.StudyCafe_R.infra.security.service.CustomOAuth2UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.util.WebUtils;

import javax.sql.DataSource;
import java.util.regex.Pattern;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;
    private final HandlerMappingIntrospector handlerMappingIntrospector;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRequestAttributeHandler cookeCsrfTokenRequestAttributeHandler = new CookieCsrfTokenRequestAttributeHandler();
        cookeCsrfTokenRequestAttributeHandler.setCsrfRequestAttributeName(null);

        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.csrfTokenRepository(new CookieCsrfTokenRepository())
                .csrfTokenRequestHandler(cookeCsrfTokenRequestAttributeHandler)
                .ignoringRequestMatchers(new SingUpRequestMatchers()));
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/index.html", "/css/**", "/js/**", "/images/**", "/static/**", "/dist/**", "/*.css", "/*.js").permitAll()
                        .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/login")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/sign-up")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/check-email-token")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/email-login")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/login-by-email")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/search/study")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/profile/*")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/xsrf-token")).permitAll()
                        .anyRequest().authenticated());


        http.rememberMe(httpSecurityRememberMeConfigurer ->
                httpSecurityRememberMeConfigurer.userDetailsService(userDetailsService)
                        .tokenRepository(tokenRepository()));

        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(
                        userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                .authorizationEndpoint(authEndpoint -> authEndpoint.authorizationRequestResolver(new CustomAuthorizationRequestResolver(clientRegistrationRepository)))
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect("/on-oauth-success");
                }));  // OAuth2

        return http.build();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices =
                new PersistentTokenBasedRememberMeServices(
                        "remember-me-key-encryption",
                        userDetailsService,
                        tokenRepository()
                );
        rememberMeServices.setTokenValiditySeconds(1209600); // 14 days
        rememberMeServices.setAlwaysRemember(true); // Set to true if you want to always remember
        return rememberMeServices;
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/node_modules/**"))
                .requestMatchers(new AntPathRequestMatcher("/favicon.ico"))
                .requestMatchers(new AntPathRequestMatcher("/resources/**"))
                .requestMatchers(new AntPathRequestMatcher("/error"))
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }


    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    private static class SingUpRequestMatchers implements RequestMatcher {

        private final Pattern allowedPath = Pattern.compile("^/sign-up$", Pattern.CASE_INSENSITIVE);

        @Override
        public boolean matches(HttpServletRequest request) {
            return allowedPath.matcher(request.getServletPath()).matches();
        }
    }

    private static class CookieCsrfTokenRequestAttributeHandler extends CsrfTokenRequestAttributeHandler {
        @Override
        public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
            Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
            if (cookie == null) {
                return null;
            }
            return cookie.getValue();
        }
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // React dev server
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow cookies or authentication

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
