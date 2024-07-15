package su.project.travel.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import su.project.travel.model.request.PlaceDetailsRequest;
import su.project.travel.model.request.PlaceRequest;
import su.project.travel.model.response.PlaceResponse;
import su.project.travel.model.response.ResponseModel;
import su.project.travel.repository.PlaceRepository;

import java.util.List;
@Slf4j
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }
    public ResponseModel<List<PlaceResponse>> getAllPlace(PlaceRequest placeRequest) {
        ResponseModel<List<PlaceResponse>> responseModel = new ResponseModel<>();
        try {
            List<PlaceResponse> placeResponses = this.placeRepository.getPlace(placeRequest);

            responseModel.setCode(200);
            responseModel.setMessage("ok");
            responseModel.setData(placeResponses);
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
            responseModel.setCode(500);
            responseModel.setMessage("server error");
        }
        return responseModel;
    }
    public ResponseModel<PlaceResponse> getPlaceDetails(PlaceDetailsRequest placeRequest) {
        ResponseModel<PlaceResponse> responseModel = new ResponseModel<>();
        if(ObjectUtils.isEmpty(placeRequest.getPlaceId())) {
            responseModel.setCode(400);
            responseModel.setMessage("placeId is null");
            return responseModel;
        }
        try {
            List<PlaceResponse> placeResponses = this.placeRepository.getPlaceDetails(placeRequest);

            responseModel.setCode(200);
            responseModel.setMessage("ok");
            responseModel.setData(placeResponses.getFirst());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
            responseModel.setCode(500);
            responseModel.setMessage("server error");
        }
        return responseModel;
    }

}
