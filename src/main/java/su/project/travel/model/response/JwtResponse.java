package su.project.travel.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Builder
public class JwtResponse {
    private String token;
}
