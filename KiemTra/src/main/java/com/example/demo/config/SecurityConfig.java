package com.example.demo.config;

import com.example.demo.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/enroll/**", "/my-courses").hasRole("STUDENT")
						.requestMatchers(
								"/", "/home", "/courses", "/register", "/login",
								"/css/**", "/js/**", "/images/**", "/uploads/**",
								"/webjars/**", "/oauth2/**", "/error")
						.permitAll()
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.defaultSuccessUrl("/home", true)
						.failureUrl("/login?error=true")
						.permitAll())
				.oauth2Login(oauth -> oauth
						.loginPage("/login")
						.successHandler(oAuth2LoginSuccessHandler)
						.failureUrl("/login?error=true"))
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login?logout=true")
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.permitAll())
				.exceptionHandling(ex -> ex
						.accessDeniedPage("/access-denied"));

		return http.build();
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authBuilder
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
		return authBuilder.build();
	}
}