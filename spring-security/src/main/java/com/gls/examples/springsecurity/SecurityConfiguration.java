package com.gls.examples.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.Filter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration{
	/*
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authz) -> 
			authz.anyRequest().authenticated()).httpBasic(withDefaults());
		return http.build();
	}
	*/

	@Bean
    public InMemoryUserDetailsManager userDetailsService() {
        /*
		UserDetails user = User.withDefaultPasswordEncoder() //Unencoded password
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        */
        UserDetails user = User.withUsername("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails userJeff = User.withUsername("jeff")
                .password("jeff")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user, userJeff);
    }

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
