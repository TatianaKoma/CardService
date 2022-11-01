package com.greedobank.cards.client;

import com.greedobank.cards.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${url}")
    private String url;

    public List<User> getUser(String email) {
        String userUrl = url + email;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<User>> response = restTemplate.exchange(
                userUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<User>>() {
                }
        );
        return response.getBody();
    }
}
