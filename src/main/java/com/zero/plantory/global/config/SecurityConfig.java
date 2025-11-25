package com.zero.plantory.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.AntPathMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final Environment env;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**", "/css/**", "/js/**", "/image/**","/data/**");
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/",
//                                "/login",
//                                "/signUp",
//                                "/members/login",
//                                "/members/signUp",
//                                "/termsOfService",
//                                "/api/members/exists"
//                        ).permitAll()
//
//                        .requestMatchers(
//                                "/community/**",
//                                "/sharing/**",
//                                "/dashboard",
//                                "/my/**"
//                        ).authenticated()
//
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(login -> login
//                        .loginPage("/login")
//                        .loginProcessingUrl("/login-process")
//                        .usernameParameter("membername")
//                        .defaultSuccessUrl("/dashboard",true)
//                        .failureUrl("/login?error=true")
//                        .permitAll()
//                )
//                .rememberMe(remember -> remember
//                        .key(env.getProperty("security.remember-me.key"))
//                        .rememberMeParameter("remember-me")
//                        .tokenValiditySeconds(60 * 60 * 24 * 7)
//                        .userDetailsService(userDetailsService)
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login")
//                );
//
//        return http.build();
//    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
