package com.greedobank.cards.service;

import com.greedobank.cards.client.UserClient;
import com.greedobank.cards.model.User;
import com.greedobank.cards.model.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserClient userClient;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userClient.getUser(email).get(0);
        return new UserWrapper(user);
    }
}
