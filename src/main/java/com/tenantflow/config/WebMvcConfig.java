package com.tenantflow.config;

import com.tenantflow.security.ApiUsageInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiUsageInterceptor apiUsageInterceptor;

    public WebMvcConfig(ApiUsageInterceptor apiUsageInterceptor) {
        this.apiUsageInterceptor = apiUsageInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the interceptor for all API endpoints except auth
        registry.addInterceptor(apiUsageInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**");
    }
}
