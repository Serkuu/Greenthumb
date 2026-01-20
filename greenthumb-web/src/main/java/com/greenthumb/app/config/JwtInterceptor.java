package com.greenthumb.app.config;

import com.greenthumb.app.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;
    //Walidacja tokena JWT
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //Pomija zapytania OPTIONS bo był błąd więc naszpąciłem
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();
        if (path.contains("/auth/") || path.contains("/error")) {
            return true;
        }

        final String authHeader = request.getHeader("Authorization");

        //Sprawdza czy nagłówek Authorization istnieje i zaczyna się od Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        final String token = authHeader.substring(7);
        try {
            //Weryfikacja tokena i wyciągnięcie nazwy użytkownika
            String username = jwtService.extractUsername(token);
            if (username != null && jwtService.isTokenValid(token, username)) {
                //Dodajemy nazwę użytkownika do atrybutów żądania
                request.setAttribute("username", username);
                return true;
            }
        } catch (Exception e) {
            //Token nieprawidłowy lub wygasł
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
