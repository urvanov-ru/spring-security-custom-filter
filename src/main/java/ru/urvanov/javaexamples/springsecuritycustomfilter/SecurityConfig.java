package ru.urvanov.javaexamples.springsecuritycustomfilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfig {
    
    @Bean
    public RequestAttributeSecurityContextRepository requestAttributeSecurityContextRepository() {
        return new RequestAttributeSecurityContextRepository();
    }
    
    @Bean
    public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
    
    @Bean
    public DelegatingSecurityContextRepository securityContextRepository(
            RequestAttributeSecurityContextRepository requestAttributeSecurityContextRepository,
            HttpSessionSecurityContextRepository httpSessionSecurityContextRepository) {
        return new DelegatingSecurityContextRepository(
                requestAttributeSecurityContextRepository,
                httpSessionSecurityContextRepository);
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
            UserDetails user = User.withDefaultPasswordEncoder()
                    .username("user")
                    .password("password")
                    .roles("USER")
                    .build();
            return new InMemoryUserDetailsManager(user);
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }
    
    @Bean
    public CustomAuthenticationProcessingFilter customAuthenticationProcessingFilter(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        CustomAuthenticationProcessingFilter filter = new CustomAuthenticationProcessingFilter(authenticationManager);
        filter.setSecurityContextRepository(securityContextRepository);
        return filter;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            AuthenticationManager authenticationManager) throws Exception {
        
        http
            .authenticationManager(authenticationManager)
            .csrf((csrf) -> csrf.disable())
            .authorizeHttpRequests( (c) -> c.requestMatchers("/rest/v1/login").permitAll())
            .authorizeHttpRequests( (c) -> c.requestMatchers("/rest/**").hasRole("USER"));
        
        return http.build();
    }
}
