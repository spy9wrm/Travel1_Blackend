package su.project.travel.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import su.project.travel.model.request.UserLoginRequest;
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
}
