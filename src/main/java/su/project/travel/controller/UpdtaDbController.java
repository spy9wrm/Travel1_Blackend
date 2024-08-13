package su.project.travel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.project.travel.model.response.ResponseModel;
import su.project.travel.model.tourism.Tourism;
import su.project.travel.service.UpdateDbService;

@RestController
@RequestMapping("/app")
@Slf4j
public class UpdtaDbController {

    private final UpdateDbService updateDbService;

    public UpdtaDbController(UpdateDbService updateDbService) {
        this.updateDbService = updateDbService;
    }

    @GetMapping("/read-json")
    public ResponseModel<Tourism> readFile() {
        String filePath = "C:/Users/spyyy/Downloads/New folder/response.json";
        return this.updateDbService.readFile(filePath);
    }
}
