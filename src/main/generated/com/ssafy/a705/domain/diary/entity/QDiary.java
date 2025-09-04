package com.ssafy.a705.domain.diary.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDiary is a Querydsl query type for Diary
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDiary extends EntityPathBase<Diary> {

    private static final long serialVersionUID = 630126273L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDiary diary = new QDiary("diary");

    public final com.ssafy.a705.global.common.QBaseEntity _super = new com.ssafy.a705.global.common.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final ListPath<com.ssafy.a705.domain.diary._image.entity.DiaryImage, com.ssafy.a705.domain.diary._image.entity.QDiaryImage> diaryImages = this.<com.ssafy.a705.domain.diary._image.entity.DiaryImage, com.ssafy.a705.domain.diary._image.entity.QDiaryImage>createList("diaryImages", com.ssafy.a705.domain.diary._image.entity.DiaryImage.class, com.ssafy.a705.domain.diary._image.entity.QDiaryImage.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> ended_at = createDate("ended_at", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.ssafy.a705.domain.location.entity.QLocation location;

    public final com.ssafy.a705.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final DatePath<java.time.LocalDate> started_at = createDate("started_at", java.time.LocalDate.class);

    public QDiary(String variable) {
        this(Diary.class, forVariable(variable), INITS);
    }

    public QDiary(Path<? extends Diary> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDiary(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDiary(PathMetadata metadata, PathInits inits) {
        this(Diary.class, metadata, inits);
    }

    public QDiary(Class<? extends Diary> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.location = inits.isInitialized("location") ? new com.ssafy.a705.domain.location.entity.QLocation(forProperty("location")) : null;
        this.member = inits.isInitialized("member") ? new com.ssafy.a705.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

