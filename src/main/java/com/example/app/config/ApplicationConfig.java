package com.example.app.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.app.login.MemberAuthFilter;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

	// バリデーションプロパティファイルの有効化
	@Override
	public Validator getValidator() {
		var validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}
	@Bean
	public ResourceBundleMessageSource messageSource() {
		var messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("validation");
		return messageSource;
	}
	// 認証用フィルタの有効化
	@Bean
	public FilterRegistrationBean<MemberAuthFilter> studentAuthFilter() {
		var bean = new FilterRegistrationBean<MemberAuthFilter>(new MemberAuthFilter());
		bean.addUrlPatterns("/");
		bean.addUrlPatterns("/doc/*");
		return bean;
	}
	// uploads フォルダを設定
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations("file:uploads/");
	}
}
