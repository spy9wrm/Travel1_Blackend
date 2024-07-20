package su.project.travel.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import su.project.travel.model.request.CreateTripRequest;
import su.project.travel.model.response.ResponseModel;
import su.project.travel.repository.TripRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class TripService {
    private final TripRepository tripRepository;

    @Autowired
    public TripService(@Qualifier("TripRepo") TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public ResponseModel<Void> addTrip(CreateTripRequest createTripRequest, Integer userId) {
        ResponseModel<Void> responseModel = new ResponseModel<>();
        try {
            Integer tripId = this.tripRepository.insertTbTrip(userId);
            for (int index = 0; index < createTripRequest.getTripDetailsList().size(); index++) {
                Integer placeId = createTripRequest.getTripDetailsList().get(index).getPlaceId();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String planDate = createTripRequest.getTripDetailsList().get(index).getPlanDate();

                this.tripRepository.insertTbTripDetails(tripId, placeId, LocalDateTime.parse(planDate, formatter));
            }
            responseModel.setCode(200);
            responseModel.setMessage("ok");
        } catch (Exception e) {
            responseModel.setCode(500);
            responseModel.setMessage(e.getMessage());
        }
        return responseModel;
    }
}
