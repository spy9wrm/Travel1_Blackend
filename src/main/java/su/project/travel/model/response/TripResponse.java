package su.project.travel.model.response;

import lombok.Getter;
import lombok.Setter;
import su.project.travel.model.common.TripDetails;

import java.util.List;

@Setter
@Getter
public class TripResponse {
    Integer tripId;
    String tripName;
    String createDate;
    List<TripDetails> placeList;
    private String isReview;
}
