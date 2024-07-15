package su.project.travel.service;

import org.springframework.stereotype.Service;
import su.project.travel.model.response.UserResponse;
import su.project.travel.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getUser(){
        List<UserResponse> listUser = this.userRepository.getUsers();
        return listUser;
    };

}
