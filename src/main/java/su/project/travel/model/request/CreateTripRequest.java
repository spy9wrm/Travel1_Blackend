package su.project.travel.model.request;

import lombok.Getter;
import lombok.Setter;
import su.project.travel.model.common.TripDetails;

import java.util.List;

@Setter
@Getter
public class CreateTripRequest {
    private Integer userId;
    private Integer tripId;
    private List<TripDetails> tripDetailsList;
}
