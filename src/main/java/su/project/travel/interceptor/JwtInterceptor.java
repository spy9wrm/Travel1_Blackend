package su.project.travel.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import su.project.travel.model.common.CurrentUser;
import su.project.travel.utils.JwtUtils;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Claims claims = JwtUtils.validateToken(token);
                if (claims != null) {
                    // Extract claims and set them into CurrentUser model
                    CurrentUser currentUser = new CurrentUser();
                    currentUser.setUserId((Integer) claims.get("userId")); // Assuming userId is Integer
                    currentUser.setUserName((String) claims.get("username")); // Assuming username is String
                    currentUser.setName((String) claims.get("name")); // Assuming name is String

                    // Set CurrentUser instance as request attribute
                    request.setAttribute("currentUser", currentUser);
                    return true; // Proceed with the request
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return false;
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return false;
            }
        } else if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return false;
        }
    }
}

