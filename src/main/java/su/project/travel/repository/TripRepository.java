package su.project.travel.repository;

import java.time.LocalDateTime;

public interface TripRepository {
    public Integer insertTbTrip(Integer userId);
    public void insertTbTripDetails(Integer tripId, Integer placeId, LocalDateTime planDate);
}
