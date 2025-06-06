package org.ezcode.codetest.infrastructure.security.config;

import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.ezcode.codetest.domain.user.service.CustomOAuth2UserService;
import org.ezcode.codetest.infrastructure.security.jwt.ExceptionHandlingFilter;
import org.ezcode.codetest.infrastructure.security.jwt.JwtFilter;
import org.ezcode.codetest.infrastructure.security.jwt.JwtUtilImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	private final JwtUtilImpl jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;
	private final CustomOAuth2UserService customOAuth2UserService; //OAuth2.0 서비스

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		JwtFilter jwtFilter = new JwtFilter(jwtUtil, redisTemplate);
		ExceptionHandlingFilter exceptionFilter = new ExceptionHandlingFilter();

		return http
			// CSRF, Form 로그인, HTTP Basic 인증 비활성화
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.oauth2Login((outh2)-> outh2
				.userInfoEndpoint((userInfoEndpointConfig ->
					userInfoEndpointConfig.userService(customOAuth2UserService))))
			// JWT 사용을 위해 세션을 STATELESS로 설정 (세션 정보 저장 x)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			//인증 URL 범위 설정
			.authorizeHttpRequests(authorizeRequests ->
				authorizeRequests
					.requestMatchers(
						"/",
						"/signin",
						"/signup",
						"/logout",
						"/login",
						"/ezlogin",
						"/oauth/**",
						"/css/**",
						"/images/**",
						"/login/oauth",
						"/login/oauth2/**", //OAuth로그인 접근
						"/actuator/**",
						"/chatting",
						"/ws/**",
						"/swagger-ui/**",
						"/swagger-resources/**",
						"/v2/**",
						"/v3/**",
						"/webjars/**",
						"/searching").permitAll()
					.requestMatchers("/admin/**").hasRole("ADMIN") //어드민 권한 필요 (문제 생성, 관리 등)
					.anyRequest().authenticated() //나머지는 일반 인증
			)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(exceptionFilter, JwtFilter.class)

			.build();
	}
}
