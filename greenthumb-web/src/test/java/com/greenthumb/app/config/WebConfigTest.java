package com.greenthumb.app.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @Mock
    private JwtInterceptor jwtInterceptor;

    @InjectMocks
    private WebConfig webConfig;

    @Mock
    private InterceptorRegistry interceptorRegistry;

    @Mock
    private CorsRegistry corsRegistry;

    @Mock
    private InterceptorRegistration interceptorRegistration;

    @Mock
    private CorsRegistration corsRegistration;


    @Test
    void addInterceptors_ShouldRegisterJwtInterceptor() {
        when(interceptorRegistry.addInterceptor(jwtInterceptor)).thenReturn(interceptorRegistration);
        when(interceptorRegistration.addPathPatterns(anyString())).thenReturn(interceptorRegistration);
        when(interceptorRegistration.excludePathPatterns(any(String[].class))).thenReturn(interceptorRegistration);

        webConfig.addInterceptors(interceptorRegistry);

        verify(interceptorRegistry).addInterceptor(jwtInterceptor);
        verify(interceptorRegistration).addPathPatterns("/api/**");
        verify(interceptorRegistration).excludePathPatterns("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html");
    }


    @Test
    void addCorsMappings_ShouldConfigureCors() {
        when(corsRegistry.addMapping(anyString())).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowCredentials(anyBoolean())).thenReturn(corsRegistration);

        webConfig.addCorsMappings(corsRegistry);

        verify(corsRegistry).addMapping("/**");
        verify(corsRegistration).allowedOrigins("http://localhost:5173", "http://localhost:5174");
        verify(corsRegistration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(corsRegistration).allowedHeaders("*");
        verify(corsRegistration).allowCredentials(true);
    }
}
