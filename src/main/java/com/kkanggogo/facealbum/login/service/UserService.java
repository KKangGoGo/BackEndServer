package com.kkanggogo.facealbum.login.service;

import com.kkanggogo.facealbum.login.dto.RequestUpdateUserInfoDto;
import com.kkanggogo.facealbum.login.model.RoleType;
import com.kkanggogo.facealbum.login.model.User;
import com.kkanggogo.facealbum.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public User signUpApi(User user) {
        //회원 가입
        String rawPassword = user.getPassword(); //입력한 password
        String encPassword = encoder.encode(rawPassword); //해싱한 password
        user.setPassword(encPassword);
        user.setRole(RoleType.USER); //db의 정보 중 role만 자동적으로 입력이 되지 않기 때문에 넣어줘야 함.
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User updateUserInfo(RequestUpdateUserInfoDto requestUpdateUserInfoDto, int id) {

        User persistanceUser = userRepository.searchIdQuery(id).orElseThrow(() -> {
            return new IllegalArgumentException("회원찾기 실패");
        });
        String rawPassword = requestUpdateUserInfoDto.getPassword();
        String encPassword = encoder.encode(rawPassword);
        persistanceUser.setPassword(encPassword);
        return persistanceUser;
    }

    @Transactional
    public User findUser(String username){
        return userRepository.searchUsername(username);
    }
}
