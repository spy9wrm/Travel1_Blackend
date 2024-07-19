package su.project.travel.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Service;
import su.project.travel.model.common.CurrentUser;
import su.project.travel.model.common.UserIdModel;
import su.project.travel.model.request.UserRegisterRequest;
import su.project.travel.model.response.JwtResponse;
import su.project.travel.model.response.ResponseModel;
import su.project.travel.repository.AuthRepository;
import su.project.travel.utils.JwtUtils;

@Service
@Slf4j
public class AuthService {
    private final AuthRepository authRepository;


    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public ResponseModel<JwtResponse> login(String username, String password) {
        ResponseModel<JwtResponse> responseModel = new ResponseModel<>();

        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            responseModel.setCode(400);
            responseModel.setMessage("username or password is empty");
            return responseModel;
        }

        try {
            CurrentUser currentUser = this.authRepository.login(username,password);
            if(currentUser == null) {
                responseModel.setCode(400);
                responseModel.setMessage("invalid username or password");
                return responseModel;
            }
            String jwtToken = JwtUtils.generateToken(username,currentUser.getUserId(),currentUser.getName());

            JwtResponse jwtResponse = JwtResponse.builder().token(jwtToken).build();

            responseModel.setCode(200);
            responseModel.setMessage("ok");
            responseModel.setData(jwtResponse);

        } catch (Exception e) {
            responseModel.setCode(500);
            responseModel.setMessage(e.getMessage());
        }
        return responseModel;
    }

    @Transactional
    public ResponseModel<Void> userRegister(UserRegisterRequest request) {
        ResponseModel<Void> responseModel = new ResponseModel<>();
        try {
            Integer userId = this.authRepository.insertTblUser(request);
            request.setUserId(userId);
            this.authRepository.insertToUserFav(request);

            responseModel.setCode(200);
            responseModel.setMessage("ok");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            responseModel.setCode(500);
            responseModel.setMessage("cannot register user");
        }
        return responseModel;
    }
}
