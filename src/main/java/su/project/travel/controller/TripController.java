package su.project.travel.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import su.project.travel.model.common.CurrentUser;
import su.project.travel.model.request.CreateTripRequest;
import su.project.travel.model.response.InquiryTripResponse;
import su.project.travel.model.response.ResponseModel;
import su.project.travel.service.TripService;

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
}
