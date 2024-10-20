package su.project.travel.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserIdRequest {
    private Integer userId;
    private Integer placeId;
}
