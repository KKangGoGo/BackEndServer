package com.kkanggogo.facealbum.login.dto;


import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGenericDto<T> {
    //user정보 수정할때도 사용되므로 이름을 바꾸는걸 추천함.
    T detailBoard;
    int nullCheck;
}
