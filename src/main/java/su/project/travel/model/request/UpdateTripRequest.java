package su.project.travel.model.request;

import lombok.Getter;
import lombok.Setter;
import su.project.travel.model.common.TripDetails;

import java.time.LocalDate;
import java.util.List;


@Setter
@Getter
public class UpdateTripRequest {
    private Integer userId;
    private Integer tripId;
    private String tripName;
    private List<TripDetails> tripDetailsList;

}
