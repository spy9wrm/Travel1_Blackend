package su.project.travel.model.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TripDetails {
    private Integer placeId;
    private String placeName;
    private String placePhoto;
    private String planDate;
}
