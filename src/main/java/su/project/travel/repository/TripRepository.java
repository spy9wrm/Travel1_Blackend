package su.project.travel.repository;

import su.project.travel.model.response.InquiryTripResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository {
    public Integer insertTbTrip(Integer userId,String tripName);
    public void insertTbTripDetails(Integer tripId, Integer placeId, LocalDateTime planDate);
    public List<InquiryTripResponse> inquiryTripResponseList(Integer userId);
}
