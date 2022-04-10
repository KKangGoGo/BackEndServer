package com.kkanggogo.facealbum.login.service;

import com.kkanggogo.facealbum.login.web.dto.RequestSignUpDto;
import com.kkanggogo.facealbum.login.web.dto.RequestUpdateUserInfoDto;
import com.kkanggogo.facealbum.login.domain.RoleType;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public User signUp(RequestSignUpDto requestSignUpDto, MultipartFile photo) {
        //회원 가입
        String rawPassword = requestSignUpDto.getPassword(); // 입력한 password
        String encPassword = encoder.encode(rawPassword); // 해싱한 password

        User user = User
                .builder()
                .username(requestSignUpDto.getUsername())
                .password(encPassword)
                .email(requestSignUpDto.getEmail())
                .role(RoleType.USER)
                .build();

        userRepository.save(user);
        return user;
    }

    // 영속성 컨텍스트에 변경내용이 등록되기 때문에, SQL문을 실행하지 않아도 transaction이 끝나면 자동으로 저장됨
    @Transactional
    public User updateUserInfo(RequestUpdateUserInfoDto requestUpdateUserInfoDto, int id) {

        User persistanceUser = userRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("회원찾기 실패");
        });

        // 비밀번호 변경
        String rawPassword = requestUpdateUserInfoDto.getPassword();
        String encPassword = encoder.encode(rawPassword);
        persistanceUser.setPassword(encPassword);
        return persistanceUser;
    }
}
