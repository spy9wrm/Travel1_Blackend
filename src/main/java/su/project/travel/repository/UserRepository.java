package su.project.travel.repository;

import su.project.travel.model.response.UserResponse;

import java.util.List;

public interface UserRepository
{
    public List<UserResponse> getUsers();
}
