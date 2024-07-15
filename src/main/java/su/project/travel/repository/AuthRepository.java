package su.project.travel.repository;

import su.project.travel.model.common.UserIdModel;

public interface AuthRepository {

    public UserIdModel login(String username, String password);
}
