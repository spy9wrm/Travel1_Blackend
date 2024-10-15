package su.project.travel.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import su.project.travel.model.common.CurrentUser;
import su.project.travel.model.common.TripDetails;
import su.project.travel.model.request.CreateTripRequest;
import su.project.travel.model.request.TripIdRequest;
import su.project.travel.model.request.UpdateTripRequest;
import su.project.travel.model.request.UserRatingRequest;
import su.project.travel.model.response.InquiryTripResponse;
import su.project.travel.model.response.ResponseModel;
import su.project.travel.model.response.TripResponse;
import su.project.travel.model.tourism.Tourism;
import su.project.travel.service.TripService;
import su.project.travel.service.UpdateDbService;

import java.util.List;

@RestController()
@RequestMapping("/app/travel/trip")
@Slf4j
@CrossOrigin("*")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }


    @PostMapping("/create")
    public ResponseModel<Void> createTrip(@RequestBody CreateTripRequest request, HttpServletRequest httpServletRequest) {
        CurrentUser currentUser = (CurrentUser) httpServletRequest.getAttribute("currentUser");
        return this.tripService.addTrip(request,currentUser.getUserId());
    }

    @PostMapping("/inquiry")
    public ResponseModel<List<InquiryTripResponse>> inquiryTrip(HttpServletRequest httpServletRequest) {
        CurrentUser currentUser = (CurrentUser) httpServletRequest.getAttribute("currentUser");
        return this.tripService.inquiryTrip(currentUser.getUserId());
    }

    @PostMapping("/delete")
    public ResponseModel<Void> deleteTrip(@RequestBody TripIdRequest tripIdRequest) {
        return this.tripService.deleteTrip(tripIdRequest);
    }

    @PostMapping("/details")
    public ResponseModel<TripResponse> getTripDetails(@RequestBody TripIdRequest tripIdRequest) {
        return this.tripService.getTripDetails(tripIdRequest);
    }
    @PostMapping("/update")
    public ResponseModel<Void> updateTrip(@RequestBody UpdateTripRequest updateTripRequest, HttpServletRequest httpServletRequest) {
        CurrentUser currentUser = (CurrentUser) httpServletRequest.getAttribute("currentUser");
    return this.tripService.updateTrip(updateTripRequest,currentUser.getUserId());
    }

    @PostMapping("/ratings")
    public ResponseModel<Void> updateTripRatings(@RequestBody List<TripDetails> tripDetails,HttpServletRequest httpServletRequest) {
        CurrentUser currentUser = (CurrentUser) httpServletRequest.getAttribute("currentUser");
        return this.tripService.updateUserRating(tripDetails,currentUser);
    }
}
