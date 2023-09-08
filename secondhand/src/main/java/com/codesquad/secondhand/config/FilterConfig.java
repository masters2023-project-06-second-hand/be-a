package com.codesquad.secondhand.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codesquad.secondhand.common.JwtFilter;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilterRegistrationBean(JwtFilter jwtFilter) {
		FilterRegistrationBean<JwtFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(jwtFilter);
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setOrder(1);
		return filterRegistrationBean;
	}
}
