package com.jeffy.springsecurityjwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfiguration {
	@Bean
    public UserDetailsService  users() {
        /*
		UserDetails user = User.withDefaultPasswordEncoder() //Unencoded password
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        */
        UserDetails user = User.builder()
        		.username("admin")
				//.password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
                .password("admin")
                .roles("ADMIN")
                .build();
        UserDetails userJeff = User.builder()
        		.username("jeff")
				//.password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
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
