package com.jeffy.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jeffy.springsecurityjwt.filters.JwtRequestFilter;

@Configuration
public class SecurityConfiguration {
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	
	/*
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authz) -> 
			authz.anyRequest().authenticated()).httpBasic(withDefaults());
		return http.build();
	}
	*/

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return (
			http.authorizeHttpRequests((authorize) -> {
				authorize.requestMatchers("/authenticate").permitAll()
					.requestMatchers("/hello-world").hasRole("USER")
					// .requestMatchers("/user").hasAnyRole("USER","ADMIN")
					// .requestMatchers("/**").hasRole("ADMIN")
					.anyRequest().denyAll();
				})
			.csrf().disable()
			.exceptionHandling().and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
			.build());
	}
	
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
