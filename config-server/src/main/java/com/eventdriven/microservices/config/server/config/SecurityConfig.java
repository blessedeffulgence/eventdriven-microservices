package com.eventdriven.microservices.config.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.Filter;

@Configuration
public class SecurityConfig  {

    @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
      return httpSecurity.antMatcher("/encrypt/**")
               .antMatcher("/decrypt/**")
               .build();
   }
}
