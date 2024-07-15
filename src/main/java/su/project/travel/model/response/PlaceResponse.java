package su.project.travel.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PlaceResponse {
    private Integer placeId;
    private String name;
    private List<String> description;
    private List<String> type;
    private List<String> photo;
    private List<String> touristType;
    private Integer maximumAttendee;
    private String hasMap;
    private Double latitude;
    private Double longitude;
    private String telephone;
    private String email;
    private List<String> streetAddress;
    private String city;
    private String citySubDivision;
    private String country;
    private String countrySubDivision;
    private String postCode;
    private List<PlaceOpeningHours> openingHours;
}
