package su.project.travel.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import su.project.travel.model.request.PlaceDetailsRequest;
import su.project.travel.model.request.PlaceRequest;
import su.project.travel.model.response.PlaceResponse;
import su.project.travel.model.response.ResponseModel;
import su.project.travel.model.response.UserResponse;
import su.project.travel.service.PlaceService;
import su.project.travel.service.UserService;

import java.util.List;

@RestController()
@RequestMapping("/app/travel/")
@Slf4j
@CrossOrigin("*")
public class PlaceController {

    private final UserService userService;
    private final PlaceService placeService;

    public PlaceController(UserService userService, PlaceService placeService) {
        this.userService = userService;
        this.placeService = placeService;
    }

    @PostMapping("getPlace")
    public ResponseModel<List<PlaceResponse>> getPlace(@RequestBody PlaceRequest placeRequest){
        log.info("PlaceController -> GetPlace");

        return this.placeService.getAllPlace(placeRequest);
    }
    @PostMapping("getPlaceDetails")
    public ResponseModel<PlaceResponse> getPlaceDetails(@RequestBody PlaceDetailsRequest placeRequest){
        log.info("PlaceController -> GetPlace");

        return this.placeService.getPlaceDetails(placeRequest);
    }
}
