package com.nnk.springboot.config;

import com.nnk.springboot.services.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configure Spring Security
 * <br>
 * Note : @EnableGlobalMethodSecurity enable @PreAuthorize annotation for
 * {@link com.nnk.springboot.controllers.UserController com.nnk.springboot.controllers.UserController}
 * <br>
 * Also contains passwordEncoder Bean
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SpringSecurityConfig {

    private final JpaUserDetailsService jpaUserDetailsService;

    public SpringSecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests(auth ->
                        auth.requestMatchers("/user/**").hasAuthority("ADMIN")
                                .anyRequest().authenticated()
                )
                .userDetailsService(jpaUserDetailsService)
                .formLogin().defaultSuccessUrl("/bidList/list").and()
                .oauth2Login().defaultSuccessUrl("/app/oauth2login", true).and()
                .logout().logoutUrl("/app-logout")
                .and().httpBasic()
                ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
