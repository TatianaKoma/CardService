package com.greedobank.cards.client;

import com.greedobank.cards.model.Role;
import com.greedobank.cards.model.User;
import com.greedobank.cards.utils.RoleTitle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserClientTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserClient userClient;

    @Value("${url}")
    private String url;

    @Test
    public void whenGetIsCalled_shouldReturnListUsers() {
        List<User> users = new ArrayList<>();
        User user = new User(1, null, null, "dzhmur@griddynamics.com", new Role(1, RoleTitle.ROLE_ADMIN));
        users.add(user);
        String email = user.getEmail();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        when(restTemplate.exchange(url + email,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<User>>() {
                }))
                .thenReturn(new ResponseEntity<List<User>>(users, HttpStatus.OK));

        List<User> actualUsers = userClient.getUser(email);
        Assertions.assertEquals(users, actualUsers);
    }
}
