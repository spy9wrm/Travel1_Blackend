package su.project.travel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import su.project.travel.model.response.ResponseModel;
import su.project.travel.model.tourism.Tourism;

import java.io.File;
import java.io.IOException;

@Service
public class UpdateDbService {
    public ResponseModel<Tourism> readFile(String filePath) {
        ResponseModel<Tourism> responseModel = new ResponseModel<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Tourism tourism = objectMapper.readValue(new File(filePath), Tourism.class);
            responseModel.setCode(200);
            responseModel.setMessage("File read successfully");

        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMessage("Failed to read file: " + e.getMessage());
        }

        return responseModel;
    }
}
