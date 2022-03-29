package com.kkanggogo.facealbum.login.controller.api;

import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.dto.RequestUpdateUserInfoDto;
import com.kkanggogo.facealbum.login.dto.ResponseDto;
import com.kkanggogo.facealbum.login.dto.ResponseGenericDto;
import com.kkanggogo.facealbum.login.model.User;
import com.kkanggogo.facealbum.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {

    @Autowired
    UserService userService;

    @PostMapping("api/signup")
    public ResponseDto<Integer> save(@RequestBody User user) {
        User checkSignUp = userService.signUpApi(user);
        if(checkSignUp != null) {
            // ("[INFO]회원가입 완료");
            return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
        }
        else {
            // ("[ERROR]회원가입 실패");
            return new ResponseDto<Integer>(HttpStatus.NO_CONTENT.value(), 0);
        }
    }

    @PutMapping("/api/mypage/update")
    public ResponseGenericDto<User> updateUser(@RequestBody RequestUpdateUserInfoDto requestUpdateUserInfoDto,
                                               @AuthenticationPrincipal PrincipalDetails principalDetails){

        // ("[INFO]유저정보 update 시도");
        //db변경
        User user = userService.updateUserInfo(requestUpdateUserInfoDto, principalDetails.getUser().getId());
        if(user != null) {
            // ("[INFO]유저정보 update 성공");
            return new ResponseGenericDto<User>(user, 1);
        }
        else {
            // ("[ERROR]유저정보 update 실패");
            return new ResponseGenericDto<User>(null, 0);
        }
    }
}
