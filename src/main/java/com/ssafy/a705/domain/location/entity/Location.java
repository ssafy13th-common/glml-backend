package com.ssafy.a705.domain.location.entity;

import com.ssafy.a705.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "locations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends BaseEntity {

    @Id
    @Comment("지역 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("지역명")
    @Column(nullable = false)
    private String name;

    @Comment("지역 코드")
    @Column(nullable = false, unique = true)
    private Integer code;

    private Location(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public static Location of(String name, Integer code) {
        return new Location(name, code);
    }

    public void updateName(String name) {
        this.name = name;
    }
}
