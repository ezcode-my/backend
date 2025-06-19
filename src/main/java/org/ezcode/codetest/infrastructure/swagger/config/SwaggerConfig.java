package org.ezcode.codetest.infrastructure.swagger.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
	info = @Info(title = "API 문서", version = "v1"),
	security = @SecurityRequirement(name = "JWT")
)
@SecurityScheme(
	name = "JWT",
	type = SecuritySchemeType.HTTP,
	scheme = "bearer",
	bearerFormat = "JWT"
)
@SecurityScheme(
	name = "JWT_REFRESH", // refreshToken용
	type = SecuritySchemeType.APIKEY,
	in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
