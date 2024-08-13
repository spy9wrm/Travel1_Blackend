package su.project.travel.model.tourism;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {
    @JsonProperty("ID")
    private Integer id;
    private double latitude;
    private double longitude;
}
