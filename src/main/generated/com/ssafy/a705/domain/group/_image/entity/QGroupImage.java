package com.ssafy.a705.domain.group._image.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupImage is a Querydsl query type for GroupImage
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupImage extends EntityPathBase<GroupImage> {

    private static final long serialVersionUID = -101339070L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupImage groupImage = new QGroupImage("groupImage");

    public final com.ssafy.a705.global.common.QBaseEntity _super = new com.ssafy.a705.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final com.ssafy.a705.domain.group._member.entity.QGroupMember groupMember;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QGroupImage(String variable) {
        this(GroupImage.class, forVariable(variable), INITS);
    }

    public QGroupImage(Path<? extends GroupImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupImage(PathMetadata metadata, PathInits inits) {
        this(GroupImage.class, metadata, inits);
    }

    public QGroupImage(Class<? extends GroupImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupMember = inits.isInitialized("groupMember") ? new com.ssafy.a705.domain.group._member.entity.QGroupMember(forProperty("groupMember"), inits.get("groupMember")) : null;
    }

}

