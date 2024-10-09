package com.travel.japan.entity;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "notice_image")
@AllArgsConstructor
@NoArgsConstructor
public class NoticeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @Column(name = "url", nullable = false)
    private String url; // 이미지 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    @ToString.Exclude  // 순환 참조 방지
    @EqualsAndHashCode.Exclude  // 순환 참조 방지
    private Notice notice; // 게시글과 연관 관계

}

