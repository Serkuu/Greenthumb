package com.greenthumb.app.config;

import com.greenthumb.app.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtInterceptorTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private JwtInterceptor jwtInterceptor;

    //Powinien zwrócić true
    @Test
    void preHandle_ShouldReturnTrue_WhenMethodIsOptions() throws Exception {
        when(request.getMethod()).thenReturn("OPTIONS");

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verifyNoInteractions(jwtService);
    }

    //Powinien zwrócić true dla tych ścieżek
    @Test
    void preHandle_ShouldReturnTrue_WhenPathIsAuth() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verifyNoInteractions(jwtService);
    }

    @Test
    void preHandle_ShouldReturnTrue_WhenPathIsError() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/error");

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verifyNoInteractions(jwtService);
    }

    //Powinien zwrócić false gdy nie ma Authorization
    @Test
    void preHandle_ShouldReturnFalse_WhenAuthHeaderIsMissing() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/gardens");
        when(request.getHeader("Authorization")).thenReturn(null);

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void preHandle_ShouldReturnFalse_WhenAuthHeaderIsInvalid() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/gardens");
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    //Powinien zwrócić true gdy token jest git
    @Test
    void preHandle_ShouldReturnTrue_WhenTokenIsValid() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/gardens");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtService.extractUsername("valid.token")).thenReturn("testuser");
        when(jwtService.isTokenValid("valid.token", "testuser")).thenReturn(true);

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verify(request).setAttribute("username", "testuser");
    }

    @Test
    void preHandle_ShouldReturnFalse_WhenTokenValidationFails() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/gardens");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtService.extractUsername("invalid.token")).thenThrow(new RuntimeException("Invalid token"));

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
