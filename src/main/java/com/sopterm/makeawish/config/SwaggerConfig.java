package com.sopterm.makeawish.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import lombok.val;

@Configuration
public class SwaggerConfig {

	@Value("${swagger.server.url}")
	private String serverUrl;

	@Bean
	public OpenAPI openAPI() {
		val info = new Info()
			.title("선물주 API")
			.version("1.0")
			.description("선물주 API Docs");

		val securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER).name("Authorization");

		val securityRequirement = new SecurityRequirement().addList("bearerAuth");

		val servers = new ArrayList<Server>();
		val server = new Server();
		server.description("실서버");
		server.setUrl(serverUrl);
		servers.add(server);

		return new OpenAPI()
			.components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
			.security(List.of(securityRequirement))
			.servers(servers)
			.info(info);

	}
}
