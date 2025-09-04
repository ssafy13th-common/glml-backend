package com.ssafy.a705.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Setter
    @CreatedDate
    @Comment("생성일")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Comment("수정일")
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Comment("삭제일")
    @Column(insertable = false)
    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        if (Objects.isNull(modifiedAt)) {
            modifiedAt = createdAt;
        }
    }

    protected void delete(LocalDateTime currentTime) {
        if (Objects.isNull(deletedAt)) {
            deletedAt = currentTime;
        }
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    protected void restore() {
        deletedAt = null;
    }

}
