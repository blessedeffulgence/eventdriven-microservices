package com.eventdriven.microservices.elastic.query.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .hasRole("USER")
                .and()
                .csrf().disable().build();
    }
     protected void configure()
     {

     }
}
