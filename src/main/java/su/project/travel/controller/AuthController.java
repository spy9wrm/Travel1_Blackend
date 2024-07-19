package su.project.travel.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import su.project.travel.model.common.CurrentUser;
import su.project.travel.model.request.UserLoginRequest;
import su.project.travel.model.request.UserRegisterRequest;
import su.project.travel.model.response.JwtResponse;
import su.project.travel.model.response.ResponseModel;
import su.project.travel.service.AuthService;

@RestController
@RequestMapping("/app/travel/auth")
@Slf4j
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseModel<JwtResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        log.info("AuthController -> Login");
        return this.authService.login(userLoginRequest.getUsername(), userLoginRequest.getPassword());
    }

    @PostMapping("/register")
    public ResponseModel<Void> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        log.info("AuthController -> Register");
        return this.authService.userRegister(userRegisterRequest);
    }

    @GetMapping("/me")
    public String getUserProfile(HttpServletRequest request) {
        CurrentUser currentUser = (CurrentUser) request.getAttribute("currentUser");
        if (currentUser != null) {
            return currentUser.getName();
        } else {
            return "Current user not found";
        }
    }


}
