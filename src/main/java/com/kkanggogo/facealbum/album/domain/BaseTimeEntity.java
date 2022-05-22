package com.kkanggogo.facealbum.album.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    // Entity가 생성된 시간 자동 저장
    @CreatedDate
    private LocalDateTime createdDate;

    // Entity가 변경된 시간 자동 저장
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
