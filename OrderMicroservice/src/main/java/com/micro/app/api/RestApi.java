package com.micro.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.micro.app.model.Food;
import com.micro.app.model.Table;
import com.micro.app.model.User;
import lombok.NoArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;
@NoArgsConstructor
public class RestApi {


    public Food getFoodByIdFromMicroservice(Integer foodId) {
        RestTemplate restTemplate = new RestTemplate();

        // Make an HTTP GET request and retrieve the response
        String url = "http://localhost:8082/api/v1/foods/" + foodId;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Food> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Food.class
        );
        // Process the retrieved data
        return response.getBody();
    }
    public Table getTableByIdFromTableMicroservice(Integer tableId) {
        RestTemplate restTemplate = new RestTemplate();

        // Make an HTTP GET request and retrieve the response
        String url = "http://localhost:8081/api/v1/tables/" + tableId;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Table> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Table.class
        );
        // Process the retrieved data
        return response.getBody();
    }
    public User getUserByIdFromUserMicroservice(Integer userId, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        // Make an HTTP GET request and retrieve the response
        String url = "http://localhost:8080/api/v1/user/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<User> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                User.class
        );
        // Process the retrieved data
        return response.getBody();
    }

    public User getUserRequest(String email){
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        restTemplate.getMessageConverters().add(converter);
        String url = "http://localhost:8080/api/v1/auth/infor";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("email", email);
        String requestBodyString = requestBody.toString();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyString, headers);
        ResponseEntity<User> responseEntity =
                restTemplate.postForEntity(url, requestEntity, User.class);

        User user = responseEntity.getBody();
        return user;
    }
    public User getUserRequest(Authentication authentication){
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        restTemplate.getMessageConverters().add(converter);
        String url = "http://localhost:8080/api/v1/auth/infor";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("email", email);
        String requestBodyString = requestBody.toString();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyString, headers);
        ResponseEntity<User> responseEntity =
                restTemplate.postForEntity(url, requestEntity, User.class);

        User user = responseEntity.getBody();
        return user;
    }
}
