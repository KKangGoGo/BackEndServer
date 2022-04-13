package com.kkanggogo.facealbum.login.web.controller.api;

import com.kkanggogo.facealbum.album.web.ImageMultipartFileRequestDtoFactory;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.web.dto.RequestSignUpDto;
import com.kkanggogo.facealbum.login.web.dto.RequestUpdateUserInfoDto;
import com.kkanggogo.facealbum.error.CustomPhotoNullException;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.web.dto.RequestSignUpDto;
import com.kkanggogo.facealbum.login.web.dto.RequestUpdateUserInfoDto;
import com.kkanggogo.facealbum.login.web.dto.ResponseAuthDto;
import com.kkanggogo.facealbum.login.web.dto.ResponseDto;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import javax.validation.constraints.Null;


@RestController
@Slf4j
public class UserApiController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    // 회원 가입
    @PostMapping("/api/signup")

    public ResponseEntity<ResponseDto<Integer>> signUp(@Valid @RequestPart(value = "photo", required = false) MultipartFile photo,
                                                       @Valid @RequestPart(value = "signupInfo") RequestSignUpDto requestSignUpDto) {

        User checkSignUp;
        log.debug("photos:{}",photo);
        if (photo == null) {
            checkSignUp = userService.signUp(requestSignUpDto);
        } else {
            checkSignUp = userService.signUp(requestSignUpDto, ImageMultipartFileRequestDtoFactory
                    .makeMultipartFileRequestDto(List.of(photo)));
        }

        if (checkSignUp != null) {
            // ("[INFO]회원가입 완료");
            return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), 1), HttpStatus.OK);
        } else {
            // ("[ERROR]회원가입 실패");
            return new ResponseEntity<>(new ResponseDto<>(HttpStatus.NO_CONTENT.value(), 0), HttpStatus.NO_CONTENT);
        }
    }
  

    // 회원 정보 수정
    @PutMapping("/api/user/mypage")
    public ResponseDto<Integer> updateUser(@Valid @RequestBody RequestUpdateUserInfoDto requestUpdateUserInfoDto,
                                               @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // ("[INFO]유저정보 update 시도");
        User user = userService.updateUserInfo(requestUpdateUserInfoDto, principalDetails.getUser().getId());
        if (user != null) {
            // ("[INFO]유저정보 update 성공");
            // return new ResponseGenericDto<User>(user, 1);
            return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
        } else {
            // ("[ERROR]유저정보 update 실패");
            //return new ResponseGenericDto<User>(null, 0);
            return new ResponseDto<Integer>(HttpStatus.NO_CONTENT.value(), 0);
        }
    }


    // 회원 정보
    @GetMapping("/api/user/auth")
    public ResponseAuthDto getAuth(@AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails != null){
            ResponseAuthDto responseAuthDto = ResponseAuthDto
                    .builder()
                    .username(principalDetails.getUsername())
                    .password(principalDetails.getPassword())
                    .email(principalDetails.getUser().getEmail())
                    .role(principalDetails.getUser().getRole())
                    .build();
            return responseAuthDto;

        }
        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }
}
