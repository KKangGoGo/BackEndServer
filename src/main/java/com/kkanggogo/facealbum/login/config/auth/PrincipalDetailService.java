package com.kkanggogo.facealbum.login.config.auth;

import com.kkanggogo.facealbum.login.model.User;
import com.kkanggogo.facealbum.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.printf("PrincipalDetailService의 loadUserByUsername");
        User user = userRepository.searchUsername(username);
        System.out.printf("user: "+user);
        System.out.printf("user서비스에서의 비번: "+user.getPassword());
        return new PrincipalDetails(user);
    }
}
