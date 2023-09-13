package com.codesquad.secondhand.config;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.codesquad.secondhand.common.filter.JwtFilter;
import com.codesquad.secondhand.common.filter.SignupTokenFilter;
import com.codesquad.secondhand.common.filter.WhiteListTokenCheckFilter;
import com.codesquad.secondhand.domain.oauth.service.OAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtFilter jwtFilter;
	private final SignupTokenFilter signupTokenFilter;
	private final WhiteListTokenCheckFilter whiteListTokenCheckFilter;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().configurationSource(corsConfigurationSource())
			.and()
			.addFilterAfter(jwtFilter, CorsFilter.class)
			.addFilterAfter(signupTokenFilter, JwtFilter.class)
			.addFilterAfter(whiteListTokenCheckFilter, SignupTokenFilter.class)
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/**").permitAll()
			.and()
			.oauth2Login()
			.successHandler(oAuth2SuccessHandler);
	}

	public CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 모든 원본 허용
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더 허용
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
