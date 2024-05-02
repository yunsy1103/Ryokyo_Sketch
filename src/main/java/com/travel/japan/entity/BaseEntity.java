package com.travel.japan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
//@MappedSuperclass 어노테이션은 공통 매핑 정보가 필요할 때 부모 클래스에 선언된 필드를 상속받는 클래스에서 그대로 사용할 때 사용한다. 이때, 부모 클래스에 대한 테이블은 별도로 생성되지 않는다.
@Getter
@Setter
public class BaseEntity {
    @JsonIgnore
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    @JsonIgnore
    private LocalDateTime deletedAt; //null일 경우 삭제가 아니니까 null인 것들만 조회를 하면 삭제된 것들은 조회가 자동으로 안 되니까 데이터는 보존하면서 조회에서 제외됨
}
