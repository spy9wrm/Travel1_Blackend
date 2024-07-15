package su.project.travel.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class PlaceOpeningHours {
    private Integer placeId;
    private String dayOfWeek;
    private String opens;
    private String closes;
}
