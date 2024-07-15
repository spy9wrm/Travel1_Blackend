package su.project.travel.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponse {
    private Integer idUser ;
    private String name;
    private LocalDate age;
    private String username;
    private String password;
    private String sex;
}
