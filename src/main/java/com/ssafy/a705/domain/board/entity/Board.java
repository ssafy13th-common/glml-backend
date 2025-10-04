package com.ssafy.a705.domain.board.entity;

import com.ssafy.a705.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @Comment("게시판 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("게시판 이름")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Comment("게시판 설명")
    private String description;

    @Comment("이미지 첨부 가능 여부")
    private boolean allow_image;

    private Board(String name, String description, boolean allow_image) {
        this.name = name;
        this.description = description;
        this.allow_image = allow_image;
    }
}
