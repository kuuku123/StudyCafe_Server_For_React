package com.StudyCafe_R.infra.config;

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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRequestAttributeHandler cookeCsrfTokenRequestAttributeHandler = new CookieCsrfTokenRequestAttributeHandler();
        cookeCsrfTokenRequestAttributeHandler.setCsrfRequestAttributeName(null);

        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.csrfTokenRepository(new CookieCsrfTokenRepository())
                .csrfTokenRequestHandler(cookeCsrfTokenRequestAttributeHandler)
                .ignoringRequestMatchers(new SingUpRequestMatchers()));
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/login")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/sign-up")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/check-email-token")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/email-login")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/login-by-email")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/search/study")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/profile/*")).permitAll()
                                .anyRequest().authenticated());

//        http.formLogin(httpSecurityFormLoginConfigurer ->
//                        httpSecurityFormLoginConfigurer.loginPage("/login").permitAll());

        http.logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer.logoutSuccessUrl("/"));

        http.rememberMe(httpSecurityRememberMeConfigurer ->
                        httpSecurityRememberMeConfigurer.userDetailsService(userDetailsService)
                                .tokenRepository(tokenRepository()));

        return http.build();
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
}
