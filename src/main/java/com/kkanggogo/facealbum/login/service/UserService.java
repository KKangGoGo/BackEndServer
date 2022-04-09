package com.kkanggogo.facealbum.login.service;

import com.kkanggogo.facealbum.album.AmazonS3Uploader;
import com.kkanggogo.facealbum.album.domain.Image;
import com.kkanggogo.facealbum.album.web.dto.ImageMultipartFileRequestDto;
import com.kkanggogo.facealbum.login.dto.RequestSignUpDto;
import com.kkanggogo.facealbum.login.dto.RequestUpdateUserInfoDto;
import com.kkanggogo.facealbum.login.model.RoleType;
import com.kkanggogo.facealbum.login.model.User;
import com.kkanggogo.facealbum.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserService {

    @Autowired
    AmazonS3Uploader userProfileAmazonS3Uploader;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public User signUp(RequestSignUpDto requestSignUpDto, ImageMultipartFileRequestDto photo) {
        User user = getUser(requestSignUpDto);

        List<Image> image = photo.toImageEntity(requestSignUpDto.getUsername());
        userProfileAmazonS3Uploader.s3Upload(image.get(0));
        user.setPhoto(image.get(0).getImagePath());

        userRepository.save(user);
        return user;
    }

    @Transactional
    public User signUp(RequestSignUpDto requestSignUpDto) {
        //회원 가입
        User user = getUser(requestSignUpDto);
        userRepository.save(user);
        return user;
    }

    private User getUser(RequestSignUpDto requestSignUpDto) {
        //회원 가입
        String rawPassword = requestSignUpDto.getPassword(); // 입력한 password
        String encPassword = encoder.encode(rawPassword); // 해싱한 password

        return User
                .builder()
                .username(requestSignUpDto.getUsername())
                .password(encPassword)
                .email(requestSignUpDto.getEmail())
                .role(RoleType.USER)
                .build();
    }

    // 영속성 컨텍스트에 변경내용이 등록되기 때문에, SQL문을 실행하지 않아도 transaction이 끝나면 자동으로 저장됨
    @Transactional
    public User updateUserInfo(RequestUpdateUserInfoDto requestUpdateUserInfoDto, Long id) {

        User persistanceUser = userRepository.searchId(id).orElseThrow(() -> {
            return new IllegalArgumentException("회원찾기 실패");
        });

        // 비밀번호 변경
        String rawPassword = requestUpdateUserInfoDto.getPassword();
        String encPassword = encoder.encode(rawPassword);
        persistanceUser.setPassword(encPassword);
        return persistanceUser;
    }
}
