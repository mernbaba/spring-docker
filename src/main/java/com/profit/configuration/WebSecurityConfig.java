package com.profit.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.profit.service.JwtUserDetailsService;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity
public class WebSecurityConfig {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	UserDetailsService userDetailsService() {

		return new JwtUserDetailsService();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(requests -> requests.requestMatchers("/authenticate", "/initial/**", "/health",
						"/.well-known/pki-validation/A3D7A0A49B297C764C02F9AF056B19E7.txt").permitAll())

				.authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//                .anyRequest().authenticated()
//                .and()
//            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		// Newely added
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.inMemoryAuthentication();
//        .withUser("admin") 
//        .password(passwordEncoder().encode("password"));
		authenticationManagerBuilder.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
		return authenticationManagerBuilder.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").maxAge(1800l).allowedOrigins("*").allowedMethods("*");
			}
		};
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		List<String> methodList = new ArrayList<>(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

		List<String> list = new ArrayList<>();
		list.add("*");
		configuration.setAllowedOrigins(list);
		// configuration.setAllowedMethods(list);
		configuration.setAllowedHeaders(list);

		configuration.setAllowedMethods(methodList);
		// configuration.setAllowedHeaders(ImmutableList.of("Content-Type",
		// "content-type", "authorization", "x-requested-with",
		// "Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
		// "x-auth-token", "x-app-id", "Origin", "Accept", "X-Requested-With",
		// "Access-Control-Request-Method", "Access-Control-Request-Headers"));

		/*
		 * list = new ArrayList<>(); list.add("Authorization");
		 * list.add("x-auth-token"); configuration.setExposedHeaders(list);
		 * configuration.setMaxAge(1800l); configuration.setAllowCredentials(true);
		 * UrlBasedCorsConfigurationSource source = new
		 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
		 * configuration); source.registerCorsConfiguration("/api/**", configuration);
		 * source.registerCorsConfiguration("/authenticate", configuration);
		 * 
		 * return source;
		 */

		configuration.setMaxAge(1800l);
		// configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		source.registerCorsConfiguration("/api/**", configuration);
		source.registerCorsConfiguration("/authenticate", configuration);
		return source;
	}
}
