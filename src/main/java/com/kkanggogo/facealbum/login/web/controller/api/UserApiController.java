package com.kkanggogo.facealbum.login.web.controller.api;

import com.kkanggogo.facealbum.album.web.ImageMultipartFileRequestDtoFactory;
import com.kkanggogo.facealbum.error.CustomExpectationFailed;
import com.kkanggogo.facealbum.error.CustomMethodArgumentNotValidException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    Logger logger = LoggerFactory.getLogger(UserApiController.class);

    // 회원 가입
    @PostMapping("/api/signup")
    public ResponseEntity<ResponseDto<Integer>> signUp(@Valid @RequestPart(value = "photo", required = false) MultipartFile photo,
                                                       @Valid @RequestPart(value = "signupInfo") RequestSignUpDto requestSignUpDto) {
        User checkSignUp;
        log.debug("photos:{}", photo);
        if (photo == null) {
            checkSignUp = userService.signUp(requestSignUpDto);
        } else {
            checkSignUp = userService.signUp(requestSignUpDto, ImageMultipartFileRequestDtoFactory
                    .makeMultipartFileRequestDto(List.of(photo)));
        }

        if (checkSignUp != null) {
            logger.info("[INFO]회원가입 완료 : {}", checkSignUp.getId());
            return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), 1), HttpStatus.OK);
        } else {

            logger.error("[ERROR]회원가입 실패", checkSignUp.getId());
            return new ResponseEntity<>(new ResponseDto<>(HttpStatus.NO_CONTENT.value(), 0), HttpStatus.NO_CONTENT);
        }
    }

    // 로그아웃
    @GetMapping("/api/logout")
    public ResponseDto<Integer> logout(HttpServletRequest request, HttpServletResponse response,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getUser() != null) {
            logger.info("[INFO]logout_user : {}", principalDetails.getUser().getId());
            new SecurityContextLogoutHandler().logout(
                    request,
                    response,
                    SecurityContextHolder.getContext().getAuthentication());
            return new ResponseDto<>(HttpStatus.OK.value(), 1);
        }
        throw new NullPointerException();
    }

    // 회원 정보 수정
    @PutMapping("/api/user/mypage")
    public ResponseDto<Integer> updateUser(@RequestPart(value = "photo", required = false) MultipartFile photo,
                                           @RequestPart(value = "updateInfo") RequestUpdateUserInfoDto requestUpdateUserInfoDto,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // ("[INFO]유저정보 update 시도");
        User user = userService.updateUserInfo(photo, requestUpdateUserInfoDto, principalDetails.getUser());
        if (user != null) {
            logger.info("[INFO]update_user : {}", principalDetails.getUser().getId());
            return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
        } else {
            logger.error("[ERROR]유저정보 update 실패");
            return new ResponseDto<Integer>(HttpStatus.NO_CONTENT.value(), 0);
        }
    }

    // 회원 정보
    @GetMapping("/api/user/auth")
    public ResponseAuthDto getAuth(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails != null) {
            logger.info("getAuth : {}", principalDetails.getUser().getId());
            ResponseAuthDto responseAuthDto = ResponseAuthDto
                    .builder()
                    .username(principalDetails.getUsername())
                    .password(principalDetails.getPassword())
                    .email(principalDetails.getUser().getEmail())
                    .role(principalDetails.getUser().getRole())
                    .build();
            return responseAuthDto;
        }
        throw new CustomExpectationFailed();
    }
}
