package su.project.travel.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PredictResponse {
    @JsonProperty("favorite_score")
    private double favoriteScore;

    @JsonProperty("final_score")
    private double finalScore;

    @JsonProperty("place_id")
    private Integer placeId;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("predicted_score")
    private double predictedScore;

    @JsonProperty("similarity_score")
    private double similarityScore;

}
