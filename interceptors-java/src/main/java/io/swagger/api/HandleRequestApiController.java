package io.swagger.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-02-23T23:20:12.397998+05:30[Asia/Colombo]")
@RestController
public class HandleRequestApiController implements HandleRequestApi {

    private static final Logger log = LoggerFactory.getLogger(HandleRequestApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public HandleRequestApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Map<String, Object>> handleRequest() {
        Map<String, Object> responseBody = new HashMap<>();
        Map<String, String> headersToAdd = new HashMap<>();
        headersToAdd.put("x-user", "admin");
        responseBody.put("headersToAdd", headersToAdd);

        String allowedAudienceStr = System.getenv("allowedAudience");
        String introspectURL = System.getenv("introspectURL");

        List<String> allowedAudience = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
                allowedAudience = objectMapper.readValue(allowedAudienceStr, new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            System.err.println("error in list: " + e.getMessage());
        }

        System.out.println(allowedAudience);
        System.out.println(introspectURL);

        // Logic to detect the unacceptable scenario
        if (allowedAudience.contains("aud1")) {
            Map<String, Object> response = new HashMap<>();

            // Prevents the body from being passed to the backend
            response.put("directRespond", true);

            // The response body for an error scenario
            Map<String, String> errorResponseBody = new HashMap<>();
            errorResponseBody.put("Status Code", "403");
            errorResponseBody.put("Error Message", "You are forbidden from accessing this resource due to invalid permissions");

            // Convert the JSON to a string for the encoding done later
            String jsonResponse = null;
            try {
                jsonResponse = objectMapper.writeValueAsString(errorResponseBody);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            // The body sent from the interceptoe needs to be base64 encoded
            String encodedResponse = Base64.getEncoder().encodeToString(jsonResponse.getBytes());
            response.put("body", encodedResponse);

            return ResponseEntity.status(200).body(response);
        }

        // return new ResponseEntity<Void>(responseBody, HttpStatus.OK);
        // return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
        return ResponseEntity.ok(responseBody);
    }

}
