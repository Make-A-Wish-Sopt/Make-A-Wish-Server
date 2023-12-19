package com.sopterm.makeawish.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.sopterm.makeawish.external")
@Configuration
public class FeignConfig {
}
