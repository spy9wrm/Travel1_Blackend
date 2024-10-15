package su.project.travel.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import su.project.travel.model.common.CurrentUser;
import su.project.travel.model.common.TripDetails;
import su.project.travel.model.request.CreateTripRequest;
import su.project.travel.model.request.TripIdRequest;
import su.project.travel.model.request.UpdateTripRequest;
import su.project.travel.model.response.InquiryTripResponse;
import su.project.travel.model.response.ResponseModel;
import su.project.travel.model.response.TripResponse;
import su.project.travel.repository.TripRepository;
import su.project.travel.utils.TranModelCachingUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class TripService {
    private final TripRepository tripRepository;

    private final TranModelCachingUtils tranModelCachingUtils;

    @Autowired
    public TripService(@Qualifier("TripRepo") TripRepository tripRepository, TranModelCachingUtils tranModelCachingUtils) {
        this.tripRepository = tripRepository;
        this.tranModelCachingUtils = tranModelCachingUtils;
    }

    @Transactional
    public ResponseModel<Void> addTrip(CreateTripRequest createTripRequest, Integer userId) {
        ResponseModel<Void> responseModel = new ResponseModel<>();
        try {
            Integer tripId = this.tripRepository.insertTbTrip(userId, createTripRequest.getTripName());
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


    public ResponseModel<Void> updateTrip(UpdateTripRequest updateTripRequest, Integer userId){
        ResponseModel<Void> responseModel = new ResponseModel<>();
        try{
            this.tripRepository.updateTbTrip(userId,updateTripRequest.getTripName(),updateTripRequest.getTripId());
            this.tripRepository.updateTbTripDetails(updateTripRequest.getTripId(),updateTripRequest.getTripDetailsList());
            responseModel.setCode(200);
            responseModel.setMessage("ok");
        }catch(Exception e){
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMessage(e.getMessage());
        }
        return responseModel;
    }

    public ResponseModel<List<InquiryTripResponse>> inquiryTrip(Integer userId) {
        ResponseModel<List<InquiryTripResponse>> responseModel = new ResponseModel<>();
        try {
            List<InquiryTripResponse> list = this.tripRepository.inquiryTripResponseList(userId);
            responseModel.setCode(200);
            responseModel.setMessage("ok");
            responseModel.setData(list);
        }catch (Exception e) {
            responseModel.setCode(500);
            responseModel.setMessage(e.getMessage());
        }
        return responseModel;
    }

    public ResponseModel<Void> deleteTrip(TripIdRequest tripIdRequest) {
        ResponseModel<Void> responseModel = new ResponseModel<>();
        if(tripIdRequest.getTripId() == null) {
            responseModel.setCode(400);
            responseModel.setMessage("require tripId");
            return responseModel;
        }
        try {
            this.tripRepository.deleteTrip(tripIdRequest.getTripId());
            responseModel.setCode(200);
            responseModel.setMessage("ok");
        }catch (Exception e) {
            responseModel.setCode(500);
            responseModel.setMessage(e.getMessage());
        }
        return responseModel;
    }


    public ResponseModel<TripResponse> getTripDetails(TripIdRequest tripIdRequest) {
        ResponseModel<TripResponse> responseModel = new ResponseModel<>();
        if(tripIdRequest.getTripId() == null) {
            responseModel.setCode(400);
            responseModel.setMessage("require tripId");
            return responseModel;
        }
        try{
            TripResponse tripResponse = this.tripRepository.getTripDetails(tripIdRequest.getTripId()).getFirst();

            responseModel.setCode(200);
            responseModel.setMessage("ok");
            responseModel.setData(tripResponse);
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            responseModel.setCode(500);
            responseModel.setMessage(e.getMessage());
        }
        return responseModel;
    }

    public ResponseModel<Void> updateUserRating(List<TripDetails> tripDetails, CurrentUser currentUser) {
        ResponseModel<Void> responseModel = new ResponseModel<>();

        try{
            this.tranModelCachingUtils.cache.remove(currentUser.getUserId());
            this.tripRepository.updateUserRating(tripDetails);
            responseModel.setCode(200);
            responseModel.setMessage("ok");
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            responseModel.setCode(500);
            responseModel.setMessage(e.getMessage());
        }
        return responseModel;
    }
}
