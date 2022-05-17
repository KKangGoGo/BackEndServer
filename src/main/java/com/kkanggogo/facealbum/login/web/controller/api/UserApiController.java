package com.kkanggogo.facealbum.login.web.controller.api;

import com.kkanggogo.facealbum.album.ImageMultipartFileRequestDtoFactory;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.service.UserService;
import com.kkanggogo.facealbum.login.web.dto.RequestSignUpDto;
import com.kkanggogo.facealbum.login.web.dto.RequestUpdateUserInfoDto;
import com.kkanggogo.facealbum.login.web.dto.ResponseAuthDto;
import com.kkanggogo.facealbum.login.web.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserApiController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    // 회원 가입

    @PostMapping("/api/signup")
    public ResponseDto<Integer> signUp(@Valid @RequestPart(value = "photo", required = false) MultipartFile photo,
                                                       @Valid @RequestPart(value = "signupInfo") RequestSignUpDto requestSignUpDto) {
        log.debug("photos:{}", photo);
        if (photo == null) {
            userService.signUp(requestSignUpDto);
        } else {
            userService.signUp(requestSignUpDto, ImageMultipartFileRequestDtoFactory
                    .makeMultipartFileRequestDto(List.of(photo)));
        }

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    // 로그아웃
    @GetMapping("/api/logout")
    public ResponseDto<Integer> logout(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getUser() != null) {
            userService.logout(principalDetails.getUsername());
        }else {
            throw new NullPointerException();
        }
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    // 회원 정보 수정
    @PutMapping("/api/user/mypage")
    public ResponseDto<Integer> updateUser(@RequestPart(value = "photo", required = false) MultipartFile photo,
                                           @RequestPart(value = "updateInfo") RequestUpdateUserInfoDto requestUpdateUserInfoDto,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // ("[INFO]유저정보 update 시도");
        User user = userService.updateUserInfo(photo, requestUpdateUserInfoDto, principalDetails.getUser());
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
    public ResponseAuthDto getAuth(HttpServletRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails != null) {
            String userPhoto = principalDetails.getUser().getPhoto();
            String photo=userPhoto!=null?userService.getImageFullPath(userPhoto):null;
            ResponseAuthDto responseAuthDto = ResponseAuthDto
                    .builder()
                    .username(principalDetails.getUsername())
                    .password(principalDetails.getPassword())
                    .email(principalDetails.getUser().getEmail())
                    .role(principalDetails.getUser().getRole())
                    .photo(photo)
                    .build();
            if((request.getAttribute("re_access_token")!=null) &&
                    (request.getAttribute("re_refresh_token")!=null)){
                responseAuthDto.setReAccessToken((String)request.getAttribute("re_access_token"));
                responseAuthDto.setReRefreshToken((String)request.getAttribute("re_refresh_token"));
            }
            return responseAuthDto;
        }
        throw new BadCredentialsException("인증된 사용자가 아닙니다.");
    }
}
