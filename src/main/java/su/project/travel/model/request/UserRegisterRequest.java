package su.project.travel.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserRegisterRequest {
    private Integer userId;
    private String username;
    private String password;
    private String birthday;
    private String name;
    private String sex;
    private List<String> typeFav;
    private String occupation;
}
