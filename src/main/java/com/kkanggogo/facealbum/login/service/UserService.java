package com.kkanggogo.facealbum.login.service;

import com.kkanggogo.facealbum.album.AmazonS3Uploader;
import com.kkanggogo.facealbum.album.ImageMultipartFileRequestDtoFactory;
import com.kkanggogo.facealbum.album.domain.Image;
import com.kkanggogo.facealbum.album.web.dto.ImageMultipartFileRequestDto;
import com.kkanggogo.facealbum.login.domain.RoleType;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.domain.repository.UserRepository;
import com.kkanggogo.facealbum.login.web.dto.RequestSignUpDto;
import com.kkanggogo.facealbum.login.web.dto.RequestUpdateUserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    AmazonS3Uploader userProfileAmazonS3Uploader;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
    public User updateUserInfo(MultipartFile multipartFile, RequestUpdateUserInfoDto requestUpdateUserInfoDto, User user) {

        User persistanceUser = userRepository.searchId(user.getId()).orElseThrow(() -> {
            return new IllegalArgumentException("회원찾기 실패");
        });

        // 기존꺼 삭제해야 함
        Optional.ofNullable(multipartFile).ifPresent(photo -> {
            ImageMultipartFileRequestDto photoDto = ImageMultipartFileRequestDtoFactory.makeMultipartFileRequestDto(List.of(photo));
            List<Image> lists = photoDto.toImageEntity(user.getUsername());
            userProfileAmazonS3Uploader.s3Upload(lists.get(0));
        });

        // 비밀번호 변경
        Optional.ofNullable(requestUpdateUserInfoDto.getPassword()).ifPresent(password -> {
            String rawPassword = password;
            String encPassword = encoder.encode(rawPassword);
            persistanceUser.setPassword(encPassword);
        });

        // 이메일 변경
        Optional.ofNullable(requestUpdateUserInfoDto.getEmail()).ifPresent(email -> {
            persistanceUser.setEmail(email);
        });

        return persistanceUser;
    }

    public String getImageFullPath(String path){
        return userProfileAmazonS3Uploader.getPrefixPath(path);
    }

    @Transactional
    public void logout(String username) {
        stringRedisTemplate.delete(username);
    }
}
