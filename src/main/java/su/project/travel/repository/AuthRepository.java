package su.project.travel.repository;

import su.project.travel.model.common.CurrentUser;
import su.project.travel.model.common.UserIdModel;
import su.project.travel.model.request.UserRegisterRequest;

public interface AuthRepository {

    public CurrentUser login(String username, String password);
    public Integer insertTblUser(UserRegisterRequest userRequest);
    public void insertToUserFav(UserRegisterRequest userRequest);
    public Integer checkusername(UserRegisterRequest userRegisterRequest) ;
}
