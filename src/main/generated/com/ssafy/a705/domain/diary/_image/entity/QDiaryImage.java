package com.ssafy.a705.domain.diary._image.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDiaryImage is a Querydsl query type for DiaryImage
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDiaryImage extends EntityPathBase<DiaryImage> {

    private static final long serialVersionUID = 1819751610L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDiaryImage diaryImage = new QDiaryImage("diaryImage");

    public final com.ssafy.a705.global.common.QBaseEntity _super = new com.ssafy.a705.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final com.ssafy.a705.domain.diary.entity.QDiary diary;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QDiaryImage(String variable) {
        this(DiaryImage.class, forVariable(variable), INITS);
    }

    public QDiaryImage(Path<? extends DiaryImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDiaryImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDiaryImage(PathMetadata metadata, PathInits inits) {
        this(DiaryImage.class, metadata, inits);
    }

    public QDiaryImage(Class<? extends DiaryImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.diary = inits.isInitialized("diary") ? new com.ssafy.a705.domain.diary.entity.QDiary(forProperty("diary"), inits.get("diary")) : null;
    }

}

