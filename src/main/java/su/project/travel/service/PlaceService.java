package su.project.travel.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import su.project.travel.addapter.PredictAdapter;
import su.project.travel.model.request.PlaceDetailsRequest;
import su.project.travel.model.request.PlaceRequest;
import su.project.travel.model.request.UserIdRequest;
import su.project.travel.model.response.PlaceResponse;
import su.project.travel.model.response.PredictResponse;
import su.project.travel.model.response.ResponseModel;
import su.project.travel.repository.PlaceRepository;

import java.util.List;
@Slf4j
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PredictAdapter predictAdapter;

    public PlaceService(PlaceRepository placeRepository, PredictAdapter predictAdapter) {
        this.placeRepository = placeRepository;
        this.predictAdapter = predictAdapter;
    }

    public ResponseModel<List<PlaceResponse>> getAllPlace(PlaceRequest placeRequest,Integer userid) {
        ResponseModel<List<PlaceResponse>> responseModel = new ResponseModel<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserIdRequest userIdRequest = new UserIdRequest();
            userIdRequest.setUserId(userid);
            String predict = this.predictAdapter.makeHttpPostRequest("http://127.0.0.1:8081/predict-places", userIdRequest);
            log.info(predict);

            List<PredictResponse> predictResponse = objectMapper.readValue(predict, new TypeReference<List<PredictResponse>>() {});
            log.info(predictResponse.getFirst().getPlaceName());

            List<PlaceResponse> placeResponses = this.placeRepository.getPlace(placeRequest,predictResponse);
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
