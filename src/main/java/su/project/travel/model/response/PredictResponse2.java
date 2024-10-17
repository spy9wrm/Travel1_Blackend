package su.project.travel.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PredictResponse2 {
    @JsonProperty("place_id")
    private Integer placeId;
}
