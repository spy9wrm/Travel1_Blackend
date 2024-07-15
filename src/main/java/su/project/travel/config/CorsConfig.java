package su.project.travel.config;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig implements CorsConfigurationSource {

    private static final List<String> ALLOWED_ORIGINS = List.of("http://localhost:4200");
    private static final List<String> ALLOWED_HEADERS = List.of("Authorization", "Content-Type");
    private static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");


    @Override
    public CorsConfiguration getCorsConfiguration(@NonNull HttpServletRequest request) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source.getCorsConfiguration(request);
    }
}

