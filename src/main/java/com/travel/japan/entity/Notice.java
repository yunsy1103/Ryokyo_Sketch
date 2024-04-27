package com.travel.japan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Member;

@Data
@Entity
@Table(name = "notices")
@EqualsAndHashCode(callSuper = false)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "writer")
    private String writer;

    @Column(name = "view_cnt")
    private Integer viewCnt;

}
