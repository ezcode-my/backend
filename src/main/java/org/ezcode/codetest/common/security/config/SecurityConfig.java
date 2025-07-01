package org.ezcode.codetest.common.security.config;

import java.util.List;

import org.ezcode.codetest.common.security.util.SecurityPath;
import org.ezcode.codetest.domain.user.service.CustomOAuth2UserService;
import org.ezcode.codetest.common.security.hander.CustomSuccessHandler;
import org.ezcode.codetest.common.security.util.ExceptionHandlingFilter;
import org.ezcode.codetest.common.security.util.JwtFilter;
import org.ezcode.codetest.common.security.util.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.DispatcherTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;
	private final CustomOAuth2UserService customOAuth2UserService; //OAuth2.0 서비스
	private final CustomSuccessHandler customSuccessHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		JwtFilter jwtFilter = new JwtFilter(jwtUtil, redisTemplate);
		ExceptionHandlingFilter exceptionFilter = new ExceptionHandlingFilter();

		return http
			// CSRF, Form 로그인, HTTP Basic 인증 비활성화
			.cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.oauth2Login((outh2)-> outh2
				.userInfoEndpoint((userInfo -> userInfo
					.userService(customOAuth2UserService)))
				.successHandler(customSuccessHandler))

			// JWT 사용을 위해 세션을 STATELESS로 설정 (세션 정보 저장 x)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			// 에러 처리 체인 추가
			.exceptionHandling(except -> except
				.authenticationEntryPoint(customAuthenticationEntryPoint())
				.accessDeniedHandler(customAccessDeniedHandler()))
			// 인증 URL 범위 설정
			.authorizeHttpRequests(authorizeRequests ->
				authorizeRequests
					.requestMatchers(new DispatcherTypeRequestMatcher(DispatcherType.ASYNC)).permitAll()
					.requestMatchers(
						SecurityPath.PUBLIC_PATH).permitAll()
					.requestMatchers(HttpMethod.GET,
						"/api/problems",
						"/api/problems/{problemId}",
						"/api/problems/*/discussions",
						"/api/problems/{problemId}/discussions/{discussionId}/replies",
						"/api/problems/{problemId}/discussions/{discussionId}/replies/**").permitAll()
					.requestMatchers("/admin/**").hasRole("ADMIN") //어드민 권한 필요 (문제 생성, 관리 등)
					.anyRequest().authenticated() //나머지는 일반 인증
			)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(exceptionFilter, JwtFilter.class)

			.build();
	}

	// 인증 실패 시 JSON 응답 반환 (HTML 리다이렉트 방지)
	@Bean
	public AuthenticationEntryPoint customAuthenticationEntryPoint() {
		return (request, response, authException) -> {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			String jsonResponse = """
                {
                    "error": "Unauthorized",
                    "message": "토큰이 필요합니다",
                    "timestamp": "%s",
                    "path": "%s"
                }
                """.formatted(
				java.time.LocalDateTime.now().toString(),
				request.getRequestURI()
			);

			response.getWriter().write(jsonResponse);
		};
	}

	// 접근 권한 없을 때 JSON 응답 반환
	@Bean
	public AccessDeniedHandler customAccessDeniedHandler() {
		return (request, response, accessDeniedException) -> {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);

			String jsonResponse = """
                {
                    "error": "Forbidden",
                    "message": "접근 권한이 없습니다",
                    "timestamp": "%s",
                    "path": "%s"
                }
                """.formatted(
				java.time.LocalDateTime.now().toString(),
				request.getRequestURI()
			);

			response.getWriter().write(jsonResponse);
		};
	}

	//CORS 설정 추가
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOriginPatterns(List.of("*"));  // patterns 를 써야됨
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);  // true 옵션 필요

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
