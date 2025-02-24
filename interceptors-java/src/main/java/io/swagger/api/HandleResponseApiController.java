package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-02-23T23:20:12.397998+05:30[Asia/Colombo]")
@RestController
public class HandleResponseApiController implements HandleResponseApi {

    private static final Logger log = LoggerFactory.getLogger(HandleResponseApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public HandleResponseApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Map<String, Object>> handleResponse() {

        Map<String, Object> responseBody = new HashMap<>();
        Map<String, String> headersToAdd = new HashMap<>();
        headersToAdd.put("x-user", "Interceptor-Service");

        Map<String, String> headersToReplace = new HashMap<>();
        headersToReplace.put("content-type", "application/json");

        responseBody.put("headersToAdd", headersToAdd);
        responseBody.put("headersToReplace", headersToReplace);
        responseBody.put("body", "eyJTdGF0dXMiOiJJbnRlcmNlcHRvciBSZXNwb25zZSJ9Cg==");

        //return new ResponseEntity<Void>(responseBody, HttpStatus.OK);
        //return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
        return ResponseEntity.ok(responseBody);
    }

}
