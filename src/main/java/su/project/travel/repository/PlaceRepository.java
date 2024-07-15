package su.project.travel.repository;

import su.project.travel.model.request.PlaceDetailsRequest;
import su.project.travel.model.request.PlaceRequest;
import su.project.travel.model.response.PlaceResponse;
import su.project.travel.model.response.UserResponse;

import java.util.List;

public interface PlaceRepository {
    public List<PlaceResponse> getPlace(PlaceRequest placeRequest);

    public List<PlaceResponse> getPlaceDetails(PlaceDetailsRequest placeDetailsRequest);
}
