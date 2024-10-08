package su.project.travel.addapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PredictAdapter {


    private final RestTemplate restTemplate;

    @Autowired
    public PredictAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String makeHttpGetRequest(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String makeHttpPostRequest(String url, Object requestPayload) {
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestPayload, String.class);
        return response.getBody();
    }

}
