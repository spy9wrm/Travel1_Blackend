package su.project.travel.repository;

import su.project.travel.model.common.TripDetails;
import su.project.travel.model.response.InquiryTripResponse;
import su.project.travel.model.response.TripResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository {
    public Integer insertTbTrip(Integer userId,String tripName);
    public void insertTbTripDetails(Integer tripId, Integer placeId, LocalDateTime planDate);
    public List<InquiryTripResponse> inquiryTripResponseList(Integer userId);
    public void deleteTrip(Integer tripId);
    public List<TripResponse> getTripDetails(Integer tripId);
    public void updateTbTrip(Integer userId,String tripName,Integer tripId);
    public void updateTbTripDetails(Integer tripId,List<TripDetails> tripDetailsList);


}
