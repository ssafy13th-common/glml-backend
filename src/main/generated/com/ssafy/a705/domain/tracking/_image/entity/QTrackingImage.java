package com.ssafy.a705.domain.tracking._image.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTrackingImage is a Querydsl query type for TrackingImage
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTrackingImage extends EntityPathBase<TrackingImage> {

    private static final long serialVersionUID = 1942644402L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTrackingImage trackingImage = new QTrackingImage("trackingImage");

    public final com.ssafy.a705.global.common.QBaseEntity _super = new com.ssafy.a705.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final com.ssafy.a705.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath trackingId = createString("trackingId");

    public QTrackingImage(String variable) {
        this(TrackingImage.class, forVariable(variable), INITS);
    }

    public QTrackingImage(Path<? extends TrackingImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTrackingImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTrackingImage(PathMetadata metadata, PathInits inits) {
        this(TrackingImage.class, metadata, inits);
    }

    public QTrackingImage(Class<? extends TrackingImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.ssafy.a705.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

