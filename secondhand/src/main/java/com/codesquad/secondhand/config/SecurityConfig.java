package com.codesquad.secondhand.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.codesquad.secondhand.domain.oauth.service.OAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final OAuth2SuccessHandler oAuth2SuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.oauth2Login()
			.redirectionEndpoint()
			.baseUri("/api/login/oauth2/code/*")
			.and()
			.successHandler(oAuth2SuccessHandler);
	}
}
