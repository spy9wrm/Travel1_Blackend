package su.project.travel.model.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InquiryTripResponse {
    private Integer tripId;
    private String tripName;
    private String photo;
    private String createDate;
}
