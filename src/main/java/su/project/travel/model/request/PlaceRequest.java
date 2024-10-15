package su.project.travel.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlaceRequest {
    private String search;
    private int size = 8;
    private int page = 0;
    private String province;
    private String type;
    private String touristType;
}
